export type BillStatus = 'DRAFT' | 'GENERATED' | 'SENT' | 'PAID' | 'OVERDUE' | 'CANCELLED' | 'REFUNDED';

export interface Bill {
  id: number;
  billNumber: string;
  meterId?: number;
  meterNumber?: string;
  organizationId?: number;
  organizationName?: string;
  customerId?: number;
  customerName?: string;
  billingPeriodStart: string;
  billingPeriodEnd: string;
  billingMonth: number;
  billingYear: number;
  dueDate?: string;
  previousReading?: number;
  currentReading?: number;
  unitsConsumed: number;
  unitRate: number;
  energyCharge: number;
  fixedCharge: number;
  taxAmount: number;
  otherCharges?: number;
  discountAmount?: number;
  totalAmount: number;
  amountPaid?: number;
  balanceDue?: number;
  status: BillStatus;
  notes?: string;
  generatedDate: string;
  paidDate?: string;
  createdAt: string;
  updatedAt: string;
}

export interface BillItem {
  id: number;
  billId: number;
  description: string;
  quantity: number;
  unitPrice: number;
  amount: number;
}

export interface GenerateBillRequest {
  meterId: number;
  billingMonth: number;
  billingYear: number;
  dueDate?: string;
  notes?: string;
}

export interface BillFilters {
  meterId?: number;
  organizationId?: number;
  status?: BillStatus;
  billingMonth?: number;
  billingYear?: number;
  query?: string;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: 'ASC' | 'DESC';
}

export interface TariffPlan {
  id: number;
  name: string;
  description?: string;
  ratePerUnit: number;
  fixedCharge: number;
  taxRate: number;
  applicableFrom: string;
  applicableTo?: string;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}
