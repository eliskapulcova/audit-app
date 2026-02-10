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
  score: number
  grade: AToFGrade
  trend: number
  previousScore: number
}

export interface KPI {
  id: KPIType
  label: string
  value: string | number
  trend: number
  isTrendPositive: boolean
  sparklineData: number[]
}

export interface SonarQubeData {
  healthStatus: HealthStatus
  bugs: number
  vulnerabilities: number
  codeSmells: number
  coverage: number
  duplications: number
  qualityGate: PassedOrFailed
  reliability: AToEGrade
  security: AToEGrade
  maintainability: AToEGrade
  severityBreakdown: {
    blocker: number
    critical: number
    major: number
    minor: number
    info: number
  }
  lastRun: string
}

export interface SemgrepData {
  healthStatus: HealthStatus
  totalFindings: number
  error: number
  warning: number
  info: number
  topCategories: { name: string; count: number }[]
  trendData: number[]
  owaspCoverage: number
  lastRun: string
}

export interface PHPCSData {
  healthStatus: HealthStatus
  totalViolations: number
  errors: number
  warnings: number
  standard: string
  topSniffs: { name: string; count: number }[]
  topFiles: { file: string; violations: number }[]
  lastRun: string
}

export interface PHPStanData {
  healthStatus: HealthStatus
  totalErrors: number
  level: number
  errorsByLevel: { level: number; count: number }[]
  topCategories: { name: string; count: number }[]
  trendData: number[]
  lastRun: string
}

export interface TrendDataPoint {
  date: string
  sonarqube: number
  semgrep: number
  phpcs: number
  phpstan: number
}

export interface Issue {
  id: string
  severity: SeverityRating
  tool: Tool
  ruleId: string
  description: string
  filePath: string
  firstDetected: string
}

export interface RepositoryHealth {
  repository: string
  healthStatuses: {
    [key in Tool]: HealthStatus
  }
}

export interface ProjectSummary {
  id: string
  name: string
  description: string
  repositoryCount: number
  healthScore: number
  healthStatus: HealthStatus
  grade: AToFGrade
  totalIssues: number
  criticalIssues: number
  coverage: number
  lastScan: string
  trend: number
  tools: Tool[]
}

export interface ProjectDetails {
  healthScore: HealthScore
  kpis: KPI[]
  sonarQubeData: SonarQubeData
  semgrepData: SemgrepData
  phpcsData: PHPCSData
  phpstanData: PHPStanData
  trendData: TrendDataPoint[]
  topIssues: Issue[]
  repositoryHealth: RepositoryHealth[]
  projectSummary: ProjectSummary
}

