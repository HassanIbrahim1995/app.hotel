export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  refreshToken?: string;
  userId: number;
  username: string;
  roles: string[];
  expiresIn: number;
  fullName: string;
}
