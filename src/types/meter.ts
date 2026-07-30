export type MeterStatus = 'ACTIVE' | 'INACTIVE' | 'MAINTENANCE' | 'ERROR' | 'DECOMMISSIONED';
export type MeterType = 'ELECTRIC' | 'GAS' | 'WATER' | 'SOLAR';
export type ConnectionType = 'SINGLE_PHASE' | 'THREE_PHASE' | 'LT' | 'HT';
export type PhaseType = 'SINGLE_PHASE' | 'THREE_PHASE';

export interface SmartMeter {
  id: string;
  meterNumber: string;
  serialNumber: string;
  manufacturer: string;
  model: string;
  meterType: MeterType;
  connectionType: ConnectionType;
  phaseType: PhaseType;
  status: MeterStatus;
  location?: string;
  latitude?: number;
  longitude?: number;
  installationDate?: string;
  lastReadingDate?: string;
  assignedUserId?: number;
  assignedUserName?: string;
  organizationId?: number;
  organizationName?: string;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface CreateMeterRequest {
  meterNumber: string;
  serialNumber: string;
  manufacturer: string;
  model: string;
  meterType: MeterType;
  connectionType: ConnectionType;
  phaseType: PhaseType;
  location?: string;
  latitude?: number;
  longitude?: number;
  installationDate?: string;
  organizationId?: number;
}

export interface UpdateMeterRequest {
  manufacturer?: string;
  model?: string;
  location?: string;
  latitude?: number;
  longitude?: number;
  connectionType?: ConnectionType;
  phaseType?: PhaseType;
  organizationId?: number;
}

export interface AssignMeterRequest {
  userId: number;
}

export interface MeterFilters {
  search?: string;
  status?: MeterStatus;
  meterType?: MeterType;
  organizationId?: number;
  isActive?: boolean;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: 'ASC' | 'DESC';
}
