export interface AuditIssue {
    source: string;
    component: string;
    file: string;
    location: string;
    fileType: string;
    author: string | null;
    rule: string;
    ruleDocLink: string;
    severity: string;
    type: string | null;
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
