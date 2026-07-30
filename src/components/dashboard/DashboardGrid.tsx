import React, { type ReactNode } from 'react';

interface DashboardGridProps {
  children: ReactNode;
  columns?: 1 | 2 | 3 | 4;
  className?: string;
}

const gridColsMap: Record<number, string> = {
  1: 'grid-cols-1',
  2: 'grid-cols-1 md:grid-cols-2',
  3: 'grid-cols-1 md:grid-cols-2 lg:grid-cols-3',
  4: 'grid-cols-1 sm:grid-cols-2 xl:grid-cols-4',
};

export const DashboardGrid: React.FC<DashboardGridProps> = ({
  children,
  columns = 4,
  className = '',
}) => {
  return (
    <div
      className={`grid gap-4 ${gridColsMap[columns]} ${className}`}
      role="list"
    >
      {React.Children.map(children, (child, index) => (
        <div key={index} role="listitem">
          {child}
        </div>
      ))}
    </div>
  );
};

export default DashboardGrid;
