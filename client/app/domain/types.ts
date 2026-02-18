export const severities = ['Critical', 'High', 'Medium', 'Low'] as const
export type SeverityRating = typeof severities[number]

export const tools = ['SonarQube', 'Semgrep', 'PHPCS', 'PHPStan'] as const
export type Tool = typeof tools[number]

export type AToEGrade = 'A' | 'B' | 'C' | 'D' | 'E'
export type AToFGrade = 'A' | 'B' | 'C' | 'D' | 'F'
export type HealthStatus = 'healthy' | 'warning' | 'critical'
export type PassedOrFailed = 'Passed' | 'Failed'

export type KPIType = 'open-issues' | 'critical-high' | 'coverage' | 'tech-debt'

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

/**
 * TODO: There are some additional metrics that could be included:
 * "Comment density": "12.1 %",
 * "Median number of lines of code per file": "31.0",
 * "Adherence to coding standard": "99.1 %"
 */
export interface SonarQubeData {
  healthStatus: HealthStatus | null // TODO: define how to calculate this
  bugs: number // issues.countBySeverityAndTypes
  vulnerabilities: number // issues.countBySeverityAndTypes
  codeSmells: number // issues.countBySeverityAndTypes
  coverage: number | null // synthesis.metrics
  technicalDebt: string | null // synthesis.detailedTechnicalDebt
  duplications: number | null // synthesis.metrics
  qualityGate: PassedOrFailed | null // TODO: define how to calculate this
  reliability: AToEGrade | null // synthesis.analysisStatus
  security: AToEGrade | null // synthesis.analysisStatus
  maintainability: AToEGrade | null // synthesis.analysisStatus
  trendData: number[]
  severityBreakdown: { // issues.countBySeverityAndTypes - add all types together
    blocker: number
    critical: number
    major: number
    minor: number
    info: number
  }
  topFiles: { file: string; violations: number }[]
  lastRun: string // date of the last analysis
}

/**
 * Skip for now
 */
export interface SemgrepData {
  healthStatus: HealthStatus
  totalFindings: number
  error: number
  warning: number
  info: number
  topCategories: { name: string; count: number }[]
  trendData: number[]
  owaspCoverage: number | null
  topFiles: { file: string; violations: number }[]
  lastRun: string
}

export interface PHPCSData {
  healthStatus: HealthStatus
  totalViolations: number // errors + warnings
  errors: number
  warnings: number
  standard: string // TODO: define what this is
  topSniffs: { name: string; count: number }[] // sniff = source (e.g. Generic.Files.LineEndings.InvalidEOLChar)
  topFiles: { file: string; violations: number }[] // files with most # of errors + warnings, TODO: this could be added to other tools as well; the path should include name of the repository
  trendData: number[]
  lastRun: string
}

export interface PHPStanData {
  healthStatus: HealthStatus
  totalErrors: number
  level: number
  errorsByLevel: { level: number; count: number }[] // in order to get this, we would probably need to run the audit on multiple different levels
  topCategories: { name: string; count: number }[]
  trendData: number[]
  topFiles: { file: string; violations: number }[]
  lastRun: string
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
  description: string
  filePath: string
  firstDetected: string
}

export interface RepositoryHealth {
  repository: string
  healthStatuses: {
    [key in Tool]: HealthStatus // TODO: define how to calculate this for each tool
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
  healthScore: HealthScore // TODO: define how to calculate this
  kpis: KPI[]
  sonarQubeData: SonarQubeData | null
  semgrepData: SemgrepData | null
  phpcsData: PHPCSData | null
  phpstanData: PHPStanData | null
  trendData: TrendDataPoint[] // should only be displayed if at least 3 points are available
  topIssues: Issue[] // 20 issues with the highest severity (pagination will not be implemented yet)
  repositoryHealth: RepositoryHealth[]
  projectSummary: ProjectSummary
}

