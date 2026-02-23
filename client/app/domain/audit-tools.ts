import type { HealthStatus, PassedOrFailed, AToEGrade } from './common'

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
