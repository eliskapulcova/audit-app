
/**
 * Keys are grades and values are Tailwind gradient classes
 */
export const healthGradeGradientColors = {
  'A': 'from-green-500 to-emerald-600',
  'B': 'from-blue-500 to-cyan-600',
  'C': 'from-yellow-500 to-orange-500',
  'D': 'from-orange-500 to-red-500',
  'F': 'from-red-500 to-rose-700',
  'default': 'from-slate-500 to-slate-600',
}

/**
 * Keys are grades and values are hex colors
 */
export const healthGradeChartColors = {
  'A': '#10b981',
  'B': '#0ea5e9',
  'C': '#d97706',
  'D': '#ef4444',
  'F': '#ef4444',
}

/**
 * Keys are ids of KPIs and values are kebab-case names of Lucide icons
 */
export const kpiIcons = {
  'total-issues': 'alert-triangle',
  'critical-high': 'alert-octagon',
  'coverage': 'shield',
  'tech-debt': 'clock',
}

export const projectHealthStatusColors = {
  'healthy': 'text-green-400 bg-green-500/10',
  'warning': 'text-yellow-400 bg-yellow-500/10',
  'critical': 'text-red-400 bg-red-500/10',
}

export const projectHealthStatusIcons = {
  'healthy': 'check-circle-2',
  'warning': 'alert-triangle',
  'critical': 'alert-triangle',
}

export const toolHealthStatuses = {
  healthy: {
      icon: 'lucide:check-circle-2',
      color: 'text-green-400',
      bg: 'bg-green-500/10',
      border: 'border-green-500',
    },
    warning: {
      icon: 'lucide:alert-triangle',
      color: 'text-yellow-400',
      bg: 'bg-yellow-500/10',
      border: 'border-yellow-500',
    },
    critical: {
      icon: 'lucide:x-circle',
      color: 'text-red-400',
      bg: 'bg-red-500/10',
      border: 'border-red-500',
    },
}

export const repositoryHealthStatusColors = {
  'healthy': 'bg-green-500 hover:bg-green-600',
  'warning': 'bg-yellow-500 hover:bg-yellow-600',
  'critical': 'bg-red-500 hover:bg-red-600',
}

export const issueSeverityColors = {
  Critical: 'bg-red-500/10 text-red-400 border-red-500',
  High: 'bg-orange-500/10 text-orange-400 border-orange-500',
  Medium: 'bg-yellow-500/10 text-yellow-400 border-yellow-500',
  Low: 'bg-blue-500/10 text-blue-400 border-blue-500',
  default: 'bg-slate-500/10 text-slate-400 border-slate-500',
}

export const issueToolColors = {
  'SonarQube': 'bg-cyan-500/10 text-cyan-400',
  'Semgrep': 'bg-green-500/10 text-green-400',
  'PHPCS': 'bg-yellow-500/10 text-yellow-400',
  'PHPStan': 'bg-red-500/10 text-red-400',
  'default': 'bg-slate-500/10 text-slate-400',
}

export const trendToolColors = {
  'SonarQube': '#06b6d4',
  'Semgrep': '#10b981',
  'PHPCS': '#f59e0b',
  'PHPStan': '#ef4444',
}
