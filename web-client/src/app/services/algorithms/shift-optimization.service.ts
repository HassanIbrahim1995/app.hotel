import { Injectable } from '@angular/core';
import { Employee } from '../../models/employee.model';
import { Shift } from '../../models/shift.model';
import { ShiftType } from '../../models/shift-type.model';
import { EmployeeShift } from '../../models/employee-shift.model';
import { VacationRequest } from '../../models/vacation-request.model';

interface ShiftAssignment {
  employee: Employee;
  shift: Shift;
  score: number;
}

@Injectable({
  providedIn: 'root'
})
export class ShiftOptimizationService {

  constructor() { }

  /**
   * Creates an optimized shift schedule based on available employees, open shifts, and constraints
   * @param employees Available employees
   * @param shifts Shifts that need to be assigned
   * @param employeeShifts Existing employee shift assignments
   * @param vacationRequests Approved vacation requests
   * @returns An optimized list of employee-shift assignments
   */
  optimizeShiftSchedule(
    employees: Employee[],
    shifts: Shift[],
    employeeShifts: EmployeeShift[],
    vacationRequests: VacationRequest[]
  ): EmployeeShift[] {
    // First, filter out employees who are on vacation for each shift date
    const availableEmployeesByShift = this.getAvailableEmployeesByShift(employees, shifts, vacationRequests);
    
    // Get existing workload for each employee to ensure balanced distribution
    const employeeWorkload = this.calculateEmployeeWorkload(employees, employeeShifts);
    
    // Get employee preferences and skills (in a real implementation, these would come from the database)
    const employeePreferences = this.getEmployeePreferences(employees);
    
    // Calculate optimal assignments using a scoring system
    const assignments: ShiftAssignment[] = [];
    
    for (const shift of shifts) {
      const shiftDate = new Date(shift.shiftDate);
      const availableEmployees = availableEmployeesByShift.get(shift.id.toString()) || [];
      
      // Score each potential assignment
      const scoredAssignments: ShiftAssignment[] = [];
      
      for (const employee of availableEmployees) {
        // Skip if employee already has a shift that day to avoid double-booking
        if (this.hasShiftOnDate(employee, shiftDate, employeeShifts)) {
          continue;
        }
        
        // Calculate score based on various factors
        const score = this.calculateAssignmentScore(employee, shift, employeeWorkload, employeePreferences);
        
        scoredAssignments.push({ employee, shift, score });
      }
      
      // Sort assignments by score (highest first)
      scoredAssignments.sort((a, b) => b.score - a.score);
      
      // Take the best assignment
      if (scoredAssignments.length > 0) {
        assignments.push(scoredAssignments[0]);
        
        // Update the employee's workload to reflect this new assignment
        const workload = employeeWorkload.get(scoredAssignments[0].employee.id.toString()) || 0;
        employeeWorkload.set(scoredAssignments[0].employee.id.toString(), workload + 1);
      }
    }
    
    // Convert assignments to EmployeeShift objects
    return assignments.map(assignment => ({
      employee: assignment.employee,
      shift: assignment.shift,
      status: 'ASSIGNED'
    } as EmployeeShift));
  }

  /**
   * Find the best employee for a shift swap request
   * @param requestingEmployee Employee requesting the swap
   * @param shiftToSwap Shift that the employee wants to swap
   * @param availableEmployees Possible employees to swap with
   * @param employeeShifts All current employee shifts
   * @param vacationRequests Approved vacation requests
   * @returns The best employee for the swap or null if none found
   */
  findOptimalShiftSwapPartner(
    requestingEmployee: Employee,
    shiftToSwap: Shift,
    availableEmployees: Employee[],
    employeeShifts: EmployeeShift[],
    vacationRequests: VacationRequest[]
  ): Employee | null {
    const shiftDate = new Date(shiftToSwap.shiftDate);
    
    // Filter out employees on vacation
    const employeesNotOnVacation = availableEmployees.filter(employee => 
      !this.isEmployeeOnVacation(employee, shiftDate, vacationRequests)
    );
    
    // Get employee preferences and skills
    const employeePreferences = this.getEmployeePreferences(employeesNotOnVacation);
    
    // Score each potential swap partner
    const scoredPartners = employeesNotOnVacation.map(employee => {
      // Calculate affinity score based on various factors
      const score = this.calculateSwapScore(requestingEmployee, employee, shiftToSwap, employeeShifts, employeePreferences);
      
      return { employee, score };
    });
    
    // Sort by score (highest first)
    scoredPartners.sort((a, b) => b.score - a.score);
    
    // Return the best partner or null if none are suitable
    return scoredPartners.length > 0 && scoredPartners[0].score > 50 ? scoredPartners[0].employee : null;
  }

  /**
   * Calculate a score for a potential shift assignment based on multiple factors
   */
  private calculateAssignmentScore(
    employee: Employee,
    shift: Shift,
    employeeWorkload: Map<string, number>,
    employeePreferences: Map<string, any>
  ): number {
    let score = 100; // Base score
    
    // Factor 1: Workload balance (lower workload = higher score)
    const workload = employeeWorkload.get(employee.id.toString()) || 0;
    score -= workload * 5; // Penalize employees who already have many shifts
    
    // Factor 2: Shift type preference
    const preferences = employeePreferences.get(employee.id.toString()) || { preferredShiftTypes: [] };
    if (preferences.preferredShiftTypes.includes(shift.shiftType.id)) {
      score += 20; // Boost score for preferred shift types
    }
    
    // Factor 3: Location preference
    if (preferences.preferredLocations && preferences.preferredLocations.includes(shift.location.id)) {
      score += 15; // Boost score for preferred locations
    }
    
    // Factor 4: Skills match (for more advanced implementations)
    // This would match employee skills to shift requirements
    
    // Factor 5: Hours constraint
    // Ensure employee doesn't exceed their maximum weekly hours
    if (workload * 8 > employee.maxHoursPerWeek - 8) { // Assuming 8 hour shifts
      score -= 50; // Heavily penalize exceeding max hours
    }
    
    return Math.max(0, score); // Ensure score is not negative
  }

  /**
   * Calculate a score for a potential shift swap based on multiple factors
   */
  private calculateSwapScore(
    requestingEmployee: Employee,
    potentialPartner: Employee,
    shiftToSwap: Shift,
    employeeShifts: EmployeeShift[],
    employeePreferences: Map<string, any>
  ): number {
    let score = 100; // Base score
    
    // Factor 1: Availability on the shift date
    const shiftDate = new Date(shiftToSwap.shiftDate);
    if (this.hasShiftOnDate(potentialPartner, shiftDate, employeeShifts)) {
      score -= 80; // Heavy penalty for already being scheduled
    }
    
    // Factor 2: Shift type preference
    const preferences = employeePreferences.get(potentialPartner.id.toString()) || { preferredShiftTypes: [] };
    if (preferences.preferredShiftTypes.includes(shiftToSwap.shiftType.id)) {
      score += 20; // Boost score for preferred shift types
    }
    
    // Factor 3: Location preference
    if (preferences.preferredLocations && preferences.preferredLocations.includes(shiftToSwap.location.id)) {
      score += 15; // Boost score for preferred locations
    }
    
    // Factor 4: Reciprocity (has the partner helped with swaps before?)
    // This would require swap history data
    
    // Factor 5: Workload balance
    const requesterShifts = employeeShifts.filter(es => es.employee.id === requestingEmployee.id).length;
    const partnerShifts = employeeShifts.filter(es => es.employee.id === potentialPartner.id).length;
    
    if (partnerShifts < requesterShifts) {
      score += 10; // Boost score for employees with lighter workloads
    }
    
    return Math.max(0, score); // Ensure score is not negative
  }

  /**
   * Generate a map of available employees for each shift after filtering out those on vacation
   */
  private getAvailableEmployeesByShift(
    employees: Employee[],
    shifts: Shift[],
    vacationRequests: VacationRequest[]
  ): Map<string, Employee[]> {
    const availableEmployeesByShift = new Map<string, Employee[]>();
    
    for (const shift of shifts) {
      const shiftDate = new Date(shift.shiftDate);
      const availableEmployees = employees.filter(employee => 
        !this.isEmployeeOnVacation(employee, shiftDate, vacationRequests)
      );
      
      availableEmployeesByShift.set(shift.id.toString(), availableEmployees);
    }
    
    return availableEmployeesByShift;
  }

  /**
   * Calculate the current workload for each employee
   */
  private calculateEmployeeWorkload(
    employees: Employee[],
    employeeShifts: EmployeeShift[]
  ): Map<string, number> {
    const workload = new Map<string, number>();
    
    // Initialize all employees with zero workload
    employees.forEach(employee => {
      workload.set(employee.id.toString(), 0);
    });
    
    // Count shifts for each employee
    employeeShifts.forEach(es => {
      const employeeId = es.employee.id.toString();
      const currentWorkload = workload.get(employeeId) || 0;
      workload.set(employeeId, currentWorkload + 1);
    });
    
    return workload;
  }

  /**
   * Check if an employee is on vacation on a specific date
   */
  private isEmployeeOnVacation(
    employee: Employee,
    date: Date,
    vacationRequests: VacationRequest[]
  ): boolean {
    return vacationRequests.some(vacation => {
      if (vacation.status !== 'APPROVED' || vacation.employee.id !== employee.id) {
        return false;
      }
      
      const startDate = new Date(vacation.startDate);
      const endDate = new Date(vacation.endDate);
      
      // Check if the shift date falls within the vacation period
      return date >= startDate && date <= endDate;
    });
  }

  /**
   * Check if an employee already has a shift on a specific date
   */
  private hasShiftOnDate(
    employee: Employee,
    date: Date,
    employeeShifts: EmployeeShift[]
  ): boolean {
    return employeeShifts.some(es => {
      if (es.employee.id !== employee.id) {
        return false;
      }
      
      const shiftDate = new Date(es.shift.shiftDate);
      // Compare only the date portion (ignore time)
      return shiftDate.toDateString() === date.toDateString();
    });
  }

  /**
   * Get employee preferences and skills (in a real implementation, this would come from the database)
   */
  private getEmployeePreferences(employees: Employee[]): Map<string, any> {
    const preferences = new Map<string, any>();
    
    // In a real implementation, this would fetch actual preference data
    // For now, we'll just generate some dummy preferences
    employees.forEach(employee => {
      preferences.set(employee.id.toString(), {
        preferredShiftTypes: [1, 2], // Assuming shift type IDs
        preferredLocations: [1],     // Assuming location IDs
        preferredDays: ['Monday', 'Tuesday', 'Wednesday'],
        skills: ['register', 'customer service', 'stocking']
      });
    });
    
    return preferences;
  }
}
