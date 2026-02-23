export const severities = ['Critical', 'High', 'Medium', 'Low'] as const
export type SeverityRating = typeof severities[number]

export const tools = ['SonarQube', 'Semgrep', 'PHPCS', 'PHPStan'] as const
export type Tool = typeof tools[number]

export type AToEGrade = 'A' | 'B' | 'C' | 'D' | 'E'
export type AToFGrade = 'A' | 'B' | 'C' | 'D' | 'F'
export type HealthStatus = 'healthy' | 'warning' | 'critical'
export type PassedOrFailed = 'Passed' | 'Failed'

export type KPIType = 'total-issues' | 'critical-high' | 'coverage' | 'tech-debt'

export interface HealthScore {
  score: number | null
  grade: AToFGrade | null
  trend: number | null
  previousScore: number | null
}

export interface KPI {
  id: KPIType
  label: string
  value: string | number
  trend: number | null
  isTrendPositive: boolean | null
  sparklineData: number[]
}

export interface TrendDataPoint {
  date: string
  tools: {
    [key in Tool]: number // value is total number of issues for that tool
  }
}

export interface Issue {
  id: string
  severity: SeverityRating
  tool: Tool
  ruleId: string
  ruleDocUrl?: string | null
  description: string
  filePath: string
  line?: number | null
  codeUrl?: string | null
  firstDetected: string
}
