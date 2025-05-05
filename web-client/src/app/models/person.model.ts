import { Address } from './address.model';

export interface Person {
  id?: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  address?: Address;
}
