export interface Location {
  id?: number;
  name: string;
  address: string;
  city: string;
  state: string;
  zipCode: string;
  country?: string;
  phoneNumber?: string;
  email?: string;
  active: boolean;
  note?: string;
}
