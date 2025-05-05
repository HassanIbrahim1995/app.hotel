import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Location } from '../../../models/location.model';
import { LocationService } from '../../../services/location.service';
import { NotificationService } from '../../../services/notification.service';

@Component({
  selector: 'app-location-list',
  templateUrl: './location-list.component.html',
  styleUrls: ['./location-list.component.scss']
})
export class LocationListComponent implements OnInit {
  locations: Location[] = [];
  selectedLocation: Location | null = null;
  isEditing = false;
  loading = false;

  // New location object
  newLocation: Location = {
    name: '',
    address: '',
    city: '',
    state: '',
    zipCode: '',
    country: '',
    phoneNumber: '',
    email: '',
    active: true,
    note: ''
  };

  constructor(
    private locationService: LocationService,
    private modalService: NgbModal,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.loadLocations();
  }

  loadLocations(): void {
    this.loading = true;
    this.locationService.getAllLocations().subscribe({
      next: (data) => {
        this.locations = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error fetching locations', error);
        this.notificationService.showError('Failed to load locations');
        this.loading = false;
      }
    });
  }

  openLocationModal(content: any, location?: Location): void {
    if (location) {
      this.selectedLocation = { ...location };
      this.isEditing = true;
    } else {
      this.newLocation = {
        name: '',
        address: '',
        city: '',
        state: '',
        zipCode: '',
        country: '',
        phoneNumber: '',
        email: '',
        active: true,
        note: ''
      };
      this.selectedLocation = null;
      this.isEditing = false;
    }

    this.modalService.open(content, { size: 'lg', centered: true });
  }

  saveLocation(): void {
    if (this.isEditing && this.selectedLocation) {
      this.locationService.updateLocation(this.selectedLocation).subscribe({
        next: () => {
          this.loadLocations();
          this.modalService.dismissAll();
          this.notificationService.showSuccess('Location updated successfully');
        },
        error: (error) => {
          console.error('Error updating location', error);
          this.notificationService.showError('Failed to update location');
        }
      });
    } else {
      this.locationService.createLocation(this.newLocation).subscribe({
        next: () => {
          this.loadLocations();
          this.modalService.dismissAll();
          this.notificationService.showSuccess('Location created successfully');
        },
        error: (error) => {
          console.error('Error creating location', error);
          this.notificationService.showError('Failed to create location');
        }
      });
    }
  }

  deleteLocation(id: number): void {
    if (confirm('Are you sure you want to delete this location?')) {
      this.locationService.deleteLocation(id).subscribe({
        next: () => {
          this.loadLocations();
          this.notificationService.showSuccess('Location deleted successfully');
        },
        error: (error) => {
          console.error('Error deleting location', error);
          this.notificationService.showError('Failed to delete location');
        }
      });
    }
  }

  toggleLocationStatus(location: Location): void {
    const updatedLocation = { ...location, active: !location.active };
    this.locationService.updateLocation(updatedLocation).subscribe({
      next: () => {
        this.loadLocations();
        this.notificationService.showSuccess(`Location ${updatedLocation.active ? 'activated' : 'deactivated'} successfully`);
      },
      error: (error) => {
        console.error('Error updating location status', error);
        this.notificationService.showError('Failed to update location status');
      }
    });
  }
}
