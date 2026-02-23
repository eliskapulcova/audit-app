import type { HealthStatus, Tool, AToFGrade, HealthScore, KPI, TrendDataPoint, Issue } from './common'
import type { SonarQubeData, SemgrepData, PHPCSData, PHPStanData } from './audit-tools'

export interface RepositoryHealth {
  repository: string
  healthStatuses: {
    [key in Tool]: HealthStatus // TODO: define how to calculate this for each tool
  }
}

export interface RepositorySummary {
  id: string
  name: string
  description: string
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

export interface RepositoryDetails {
  healthScore: HealthScore // TODO: define how to calculate this
  kpis: KPI[]
  sonarQubeData: SonarQubeData | null
  semgrepData: SemgrepData | null
  phpcsData: PHPCSData | null
  phpstanData: PHPStanData | null
  trendData: TrendDataPoint[] // should only be displayed if at least 3 points are available
  topIssues: Issue[] // 20 issues with the highest severity (pagination will not be implemented yet)
  repositorySummary: RepositorySummary
}
