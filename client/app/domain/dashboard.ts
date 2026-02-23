import type { ProjectSummary } from './project'

export interface DashboardData {
  totalProjects: number
  avgHealthScore: number
  totalRepositories: number
  criticalIssues: number
  projectSummaries: ProjectSummary[]
}
