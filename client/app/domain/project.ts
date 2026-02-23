import type { HealthStatus, AToFGrade, Tool, HealthScore, KPI, TrendDataPoint, Issue } from './common'
import type { SonarQubeData, SemgrepData, PHPCSData, PHPStanData } from './audit-tools'
import type { RepositoryHealth } from './repository'

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
