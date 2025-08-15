export interface UserProfile {
  id: string;
  email: string;
  firstName?: string;
  lastName?: string;
  phone?: string;
  role?: string;
  avatarUrl?: string;
  company?: string;
}

export interface UpdateUserProfileRequest {
  firstName?: string;
  lastName?: string;
  email?: string;
  phone?: string;
  company?: string;
}

export interface ChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
}


