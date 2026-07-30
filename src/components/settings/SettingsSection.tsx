import React, { type ReactNode } from 'react';

interface SettingsSectionProps {
  title: string;
  description?: string;
  icon?: ReactNode;
  children: ReactNode;
  divider?: boolean;
  actions?: ReactNode;
}

export const SettingsSection: React.FC<SettingsSectionProps> = ({
  title,
  description,
  icon,
  children,
  divider = true,
  actions,
}) => {
  return (
    <section
      className={`py-6 ${divider ? 'border-b border-gray-200 dark:border-gray-700' : ''}`}
    >
      <div className="flex items-start justify-between mb-4">
        <div className="flex items-start gap-3">
          {icon && (
            <div className="mt-0.5 rounded-lg bg-gray-100 p-2 text-gray-600 dark:bg-gray-700 dark:text-gray-400">
              {icon}
            </div>
          )}
          <div>
            <h3 className="text-base font-semibold text-gray-900 dark:text-white">
              {title}
            </h3>
            {description && (
              <p className="mt-0.5 text-sm text-gray-500 dark:text-gray-400">
                {description}
              </p>
            )}
          </div>
        </div>
        {actions && (
          <div className="flex-shrink-0">{actions}</div>
        )}
      </div>
      <div className="pl-0 sm:pl-11">
        {children}
      </div>
    </section>
  );
};

export default SettingsSection;
