export type ReadingType = 'MANUAL' | 'AUTOMATIC' | 'ESTIMATED' | 'REMOTE';
export type QualityStatus = 'VALID' | 'SUSPICIOUS' | 'ESTIMATED' | 'MISSING';

export interface EnergyReading {
  id: number;
  meterId: number;
  meterNumber?: string;
  readingValue: number;
  readingUnit: string;
  readingTimestamp: string;
  readingType: ReadingType;
  qualityStatus: QualityStatus;
  source?: string;
  remarks?: string;
  createdBy?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateEnergyReadingRequest {
  meterId: number;
  readingValue: number;
  readingUnit?: string;
  readingTimestamp: string;
  readingType: ReadingType;
  qualityStatus?: QualityStatus;
  source?: string;
  remarks?: string;
}

export interface UpdateEnergyReadingRequest {
  readingValue?: number;
  readingTimestamp?: string;
  readingType?: ReadingType;
  qualityStatus?: QualityStatus;
  remarks?: string;
}

export interface ConsumptionResponse {
  meterId: number;
  meterNumber?: string;
  totalConsumption: number;
  unit: string;
  startDate: string;
  endDate: string;
  averageDailyConsumption?: number;
  estimatedCost?: number;
  readingCount: number;
}

export interface EnergyReadingFilters {
  meterId?: number;
  readingType?: ReadingType;
  qualityStatus?: QualityStatus;
  startDate?: string;
  endDate?: string;
  searchKeyword?: string;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: 'ASC' | 'DESC';
}
