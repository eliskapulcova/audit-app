import type { ReportTool } from "./report-tool";

export interface AuditIssue {
    tool: ReportTool;
    location: string;
    file: string;
    fileType: string;
    line: number;
    author: string | null;
    rule: string;
    ruleDocLink: string | null;
    severity: string;
    issueType: string | null;
    impacts: {
        softwareQuality?: string;
        severity?: string;
        [key: string]: string | undefined;
    };
    tags: string[];
    message: string;
    quickFixAvailable: boolean;
    effort: string | null;
}
