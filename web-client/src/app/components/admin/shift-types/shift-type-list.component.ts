import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ShiftType } from '../../../models/shift-type.model';
import { ShiftTypeService } from '../../../services/shift-type.service';
import { NotificationService } from '../../../services/notification.service';

@Component({
  selector: 'app-shift-type-list',
  templateUrl: './shift-type-list.component.html',
  styleUrls: ['./shift-type-list.component.scss']
})
export class ShiftTypeListComponent implements OnInit {
  shiftTypes: ShiftType[] = [];
  selectedShiftType: ShiftType | null = null;
  isEditing = false;
  loading = false;

  // New shift type object
  newShiftType: ShiftType = {
    name: '',
    description: '',
    defaultStartTime: '09:00',
    defaultEndTime: '17:00',
    color: '#3788d8',
    active: true
  };

  // Available colors for shift types
  availableColors = [
    { value: '#3788d8', name: 'Blue' },
    { value: '#4caf50', name: 'Green' },
    { value: '#f44336', name: 'Red' },
    { value: '#ff9800', name: 'Orange' },
    { value: '#9c27b0', name: 'Purple' },
    { value: '#795548', name: 'Brown' },
    { value: '#607d8b', name: 'Gray' },
    { value: '#ffeb3b', name: 'Yellow' }
  ];

  constructor(
    private shiftTypeService: ShiftTypeService,
    private modalService: NgbModal,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.loadShiftTypes();
  }

  loadShiftTypes(): void {
    this.loading = true;
    this.shiftTypeService.getAllShiftTypes().subscribe({
      next: (data) => {
        this.shiftTypes = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error fetching shift types', error);
        this.notificationService.showError('Failed to load shift types');
        this.loading = false;
      }
    });
  }

  openShiftTypeModal(content: any, shiftType?: ShiftType): void {
    if (shiftType) {
      this.selectedShiftType = { ...shiftType };
      this.isEditing = true;
    } else {
      this.newShiftType = {
        name: '',
        description: '',
        defaultStartTime: '09:00',
        defaultEndTime: '17:00',
        color: '#3788d8',
        active: true
      };
      this.selectedShiftType = null;
      this.isEditing = false;
    }

    this.modalService.open(content, { centered: true });
  }

  saveShiftType(): void {
    if (this.isEditing && this.selectedShiftType) {
      this.shiftTypeService.updateShiftType(this.selectedShiftType).subscribe({
        next: () => {
          this.loadShiftTypes();
          this.modalService.dismissAll();
          this.notificationService.showSuccess('Shift type updated successfully');
        },
        error: (error) => {
          console.error('Error updating shift type', error);
          this.notificationService.showError('Failed to update shift type');
        }
      });
    } else {
      this.shiftTypeService.createShiftType(this.newShiftType).subscribe({
        next: () => {
          this.loadShiftTypes();
          this.modalService.dismissAll();
          this.notificationService.showSuccess('Shift type created successfully');
        },
        error: (error) => {
          console.error('Error creating shift type', error);
          this.notificationService.showError('Failed to create shift type');
        }
      });
    }
  }

  deleteShiftType(id: number): void {
    if (confirm('Are you sure you want to delete this shift type?')) {
      this.shiftTypeService.deleteShiftType(id).subscribe({
        next: () => {
          this.loadShiftTypes();
          this.notificationService.showSuccess('Shift type deleted successfully');
        },
        error: (error) => {
          console.error('Error deleting shift type', error);
          this.notificationService.showError('Failed to delete shift type');
        }
      });
    }
  }

  toggleShiftTypeStatus(shiftType: ShiftType): void {
    const updatedShiftType = { ...shiftType, active: !shiftType.active };
    this.shiftTypeService.updateShiftType(updatedShiftType).subscribe({
      next: () => {
        this.loadShiftTypes();
        this.notificationService.showSuccess(`Shift type ${updatedShiftType.active ? 'activated' : 'deactivated'} successfully`);
      },
      error: (error) => {
        console.error('Error updating shift type status', error);
        this.notificationService.showError('Failed to update shift type status');
      }
    });
  }

  getColorName(colorCode: string): string {
    const color = this.availableColors.find(c => c.value === colorCode);
    return color ? color.name : 'Custom';
  }
}
