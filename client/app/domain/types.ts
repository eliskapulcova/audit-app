export interface HealthScore {
  score: number;
  grade: 'A' | 'B' | 'C' | 'D' | 'F';
  trend: number;
  previousScore: number;
}

export interface KPI {
  id: string;
  label: string;
  value: string | number;
  trend: number;
  sparklineData: number[];
  icon: string;
}

export interface SonarQubeData {
  bugs: number;
  vulnerabilities: number;
  codeSmells: number;
  coverage: number;
  duplications: number;
  qualityGate: 'Passed' | 'Failed';
  reliability: 'A' | 'B' | 'C' | 'D' | 'E';
  security: 'A' | 'B' | 'C' | 'D' | 'E';
  maintainability: 'A' | 'B' | 'C' | 'D' | 'E';
  severityBreakdown: {
    blocker: number;
    critical: number;
    major: number;
    minor: number;
    info: number;
  };
  lastRun: string;
}

export interface SemgrepData {
  totalFindings: number;
  error: number;
  warning: number;
  info: number;
  topCategories: { name: string; count: number }[];
  trendData: number[];
  owaspCoverage: number;
  lastRun: string;
}

export interface PHPCSData {
  totalViolations: number;
  errors: number;
  warnings: number;
  standard: string;
  topSniffs: { name: string; count: number }[];
  topFiles: { file: string; violations: number }[];
  lastRun: string;
}

export interface PHPStanData {
  totalErrors: number;
  level: number;
  errorsByLevel: { level: number; count: number }[];
  topCategories: { name: string; count: number }[];
  trendData: number[];
  lastRun: string;
}

export interface TrendDataPoint {
  date: string;
  sonarqube: number;
  semgrep: number;
  phpcs: number;
  phpstan: number;
}

export interface Issue {
  id: string;
  severity: 'Critical' | 'High' | 'Medium' | 'Low';
  tool: 'SonarQube' | 'Semgrep' | 'PHPCS' | 'PHPStan';
  ruleId: string;
  description: string;
  filePath: string;
  firstDetected: string;
}

export interface RepositoryHealth {
  repository: string;
  sonarqube: 'healthy' | 'warning' | 'critical';
  semgrep: 'healthy' | 'warning' | 'critical';
  phpcs: 'healthy' | 'warning' | 'critical';
  phpstan: 'healthy' | 'warning' | 'critical';
}

export interface ProjectSummary {
  id: string;
  name: string;
  description: string;
  repositoryCount: number;
  healthScore: number;
  grade: 'A' | 'B' | 'C' | 'D' | 'F';
  totalIssues: number;
  criticalIssues: number;
  coverage: number;
  lastScan: string;
  trend: number;
  toolsActive: number;
}
