export interface Profile {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  avatar?: string;
  role: string;
  department?: string;
  organization?: string;
  createdAt: string;
  lastLogin?: string;
}

export interface ProfileUpdatePayload {
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  department?: string;
}

export interface ChangePasswordPayload {
  currentPassword: string;
  newPassword: string;
  confirmPassword: string;
}
