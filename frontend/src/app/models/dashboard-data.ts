export interface DashboardData {
  sidebar: SidebarData;
  navbar: NavbarData;
  userProfile: UserProfileData;
  quickActions: QuickAction[];
  notifications: Notification[];
  stats: StatItem[];
}

export interface SidebarData {
  logo: {
    imageUrl: string;
    imageAlt: string;
    title: string;
    subtitle: string;
  };
  menuItems: MenuItem[];
  userCard: UserCardData;
}

export interface MenuItem {
  id: string;
  text: string;
  icon: string;
  route: string;
  order: number;
  isActive: boolean;
  children?: MenuItem[];
  permissions?: string[];
}

export interface UserCardData {
  avatarUrl: string;
  userName: string;
  userEmail: string;
  userRole: string;
  profileLink: string;
  settingsLink: string;
}

export interface NavbarData {
  searchPlaceholder: string;
  searchShortcut: string;
  quickActions: QuickAction[];
  themeToggle: ThemeToggleData;
  notifications: Notification[];
  userMenu: UserMenuData;
}

export interface QuickAction {
  id: string;
  text: string;
  icon: string;
  url: string;
  order: number;
  category: string;
}

export interface ThemeToggleData {
  lightIcon: string;
  darkIcon: string;
  lightText: string;
  darkText: string;
}

export interface Notification {
  id: string;
  title: string;
  message: string;
  type: 'info' | 'success' | 'warning' | 'error' | 'message' | 'user' | 'system' | 'security' | 'payment' | 'invoice' | 'quote';
  timestamp: Date;
  isRead: boolean;
  actionUrl?: string;
  order: number;
  tags?: string[];
  actions?: NotificationAction[];
  avatarUrl?: string;
  senderName?: string;
}

export interface NotificationAction {
  type: 'primary' | 'secondary' | 'success' | 'danger' | 'warning';
  label: string;
  action: string;
}

export interface UserMenuData {
  profileLink: string;
  settingsLink: string;
  logoutText: string;
}

export interface UserProfileData {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  role: string;
  avatarUrl: string;
  company?: string;
  phone?: string;
  address?: string;
  preferences: UserPreferences;
}

export interface UserPreferences {
  theme: 'light' | 'dark' | 'auto';
  language: string;
  timezone: string;
  notifications: NotificationSettings;
}

export interface NotificationSettings {
  email: boolean;
  push: boolean;
  sms: boolean;
  frequency: 'immediate' | 'daily' | 'weekly';
}

export interface StatItem {
  id: string;
  title: string;
  value: number | string;
  change: number;
  changeType: 'increase' | 'decrease' | 'neutral';
  icon: string;
  color: string;
  order: number;
}
