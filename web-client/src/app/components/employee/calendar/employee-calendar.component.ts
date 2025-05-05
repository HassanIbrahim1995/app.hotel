import { Component, OnInit } from '@angular/core';
import { CalendarOptions, EventClickArg } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import listPlugin from '@fullcalendar/list';
import interactionPlugin from '@fullcalendar/interaction';
import { CalendarService } from '../../../services/calendar.service';
import { ShiftService } from '../../../services/shift.service';
import { CalendarEntry } from '../../../models/calendar-entry.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

// Declare Bootstrap Modal for event details
declare var bootstrap: any;

@Component({
  selector: 'app-employee-calendar',
  templateUrl: './employee-calendar.component.html',
  styleUrls: ['./employee-calendar.component.scss']
})
export class EmployeeCalendarComponent implements OnInit {
  calendarOptions: CalendarOptions = {
    initialView: 'dayGridMonth',
    plugins: [dayGridPlugin, timeGridPlugin, listPlugin, interactionPlugin],
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: ''
    },
    events: [],
    eventClick: this.handleEventClick.bind(this),
    height: 'auto',
    contentHeight: 'auto'
  };
  
  currentView = 'dayGridMonth';
  calendarEntries: CalendarEntry[] = [];
  upcomingEvents: any[] = [];
  selectedEvent: any = null;
  eventModal: any;
  
  legendItems = [
    { name: 'Day Shift', color: '#3498db' },
    { name: 'Evening Shift', color: '#f39c12' },
    { name: 'Night Shift', color: '#8e44ad' },
    { name: 'Vacation', color: '#2ecc71' },
    { name: 'Holiday', color: '#e74c3c' }
  ];

  constructor(
    private calendarService: CalendarService,
    private shiftService: ShiftService,
    private modalService: NgbModal
  ) { }

  ngOnInit(): void {
    this.loadCalendarData();
  }

  loadCalendarData(): void {
    this.calendarService.getPersonalCalendar().subscribe(entries => {
      this.calendarEntries = entries;
      this.formatCalendarEvents();
    });
  }

  formatCalendarEvents(): void {
    const events = this.calendarEntries.map(entry => {
      return {
        id: entry.id?.toString(),
        title: entry.title,
        start: entry.startDate,
        end: entry.endDate,
        color: entry.color,
        allDay: entry.allDay,
        description: entry.description,
        location: entry.shift?.location?.name,
        extendedProps: {
          entryType: entry.entryType,
          id: entry.id,
          // Shift-specific properties
          shiftType: entry.shift?.shiftType?.name,
          status: entry.shift ? 'ASSIGNED' : null, // This would come from the API in the real implementation
          assignedBy: 'Manager Name', // This would come from the API in the real implementation
          // Vacation-specific properties
          requestDate: entry.entryType === 'VACATION' ? new Date() : null, // This would come from the API
          status: entry.entryType === 'VACATION' ? 'APPROVED' : null, // This would come from the API
          reviewedBy: entry.entryType === 'VACATION' ? 'Manager Name' : null, // This would come from the API
          reviewNotes: entry.entryType === 'VACATION' ? 'Vacation approved' : null // This would come from the API
        }
      };
    });
    
    this.calendarOptions.events = events;
    this.setUpcomingEvents(events);
  }

  setUpcomingEvents(events: any[]): void {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    this.upcomingEvents = events
      .filter(event => {
        const eventStart = new Date(event.start);
        return eventStart >= today;
      })
      .sort((a, b) => {
        return new Date(a.start).getTime() - new Date(b.start).getTime();
      })
      .slice(0, 5); // Show only the next 5 events
  }

  handleEventClick(clickInfo: EventClickArg): void {
    this.selectedEvent = clickInfo.event;
    const modalElement = document.getElementById('eventDetailsModal');
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    }
  }

  changeView(view: string): void {
    this.currentView = view;
    this.calendarOptions.initialView = view;
  }

  getStatusBadgeClass(status: string): string {
    if (!status) return 'bg-secondary';
    
    switch (status.toUpperCase()) {
      case 'ASSIGNED': return 'bg-warning text-dark';
      case 'CONFIRMED': return 'bg-success';
      case 'DECLINED': return 'bg-danger';
      case 'COMPLETED': return 'bg-info';
      default: return 'bg-secondary';
    }
  }

  getVacationStatusBadgeClass(status: string): string {
    if (!status) return 'bg-secondary';
    
    switch (status.toUpperCase()) {
      case 'PENDING': return 'bg-warning text-dark';
      case 'APPROVED': return 'bg-success';
      case 'REJECTED': return 'bg-danger';
      default: return 'bg-secondary';
    }
  }

  confirmShift(id: number): void {
    if (!id) return;
    
    this.shiftService.confirmShift(id).subscribe(() => {
      // Update the local data
      if (this.selectedEvent) {
        this.selectedEvent.extendedProps.status = 'CONFIRMED';
      }
      
      // Close the modal
      const modalElement = document.getElementById('eventDetailsModal');
      if (modalElement) {
        const modal = bootstrap.Modal.getInstance(modalElement);
        if (modal) {
          modal.hide();
        }
      }
      
      // Reload the calendar data
      this.loadCalendarData();
    });
  }

  declineShift(id: number): void {
    if (!id) return;
    
    this.shiftService.declineShift(id, 'Unable to work this shift').subscribe(() => {
      // Update the local data
      if (this.selectedEvent) {
        this.selectedEvent.extendedProps.status = 'DECLINED';
      }
      
      // Close the modal
      const modalElement = document.getElementById('eventDetailsModal');
      if (modalElement) {
        const modal = bootstrap.Modal.getInstance(modalElement);
        if (modal) {
          modal.hide();
        }
      }
      
      // Reload the calendar data
      this.loadCalendarData();
    });
  }
}
