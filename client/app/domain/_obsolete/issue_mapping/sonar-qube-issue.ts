export interface SonarQubeIssue {
    component: string;
    line: number;
    author: string;
    rule: string;
    severity: string;
    type: string;
    impacts: string;
    tags: string;
    message: string;
    quickFixAvailable: boolean;
    effort: string;
    [key: string]: any;
}
