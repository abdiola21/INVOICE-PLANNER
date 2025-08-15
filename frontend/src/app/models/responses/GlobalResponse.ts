export interface GlobalResponse<T> {
  timestamp: Date;
  error: boolean;
  message: string;
  data: T;
}

