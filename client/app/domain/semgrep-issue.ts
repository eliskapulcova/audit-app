export interface SemgrepIssue {
    path: string;
    start: {
        line: number;
        col?: number;
    };
    end?: {
        line: number;
        col?: number;
    };
    extra: {
        message: string;
        severity: string;
        fix?: string;
        metadata?: {
            cwe?: string[];
            source?: string;
            category?: string;
            impact?: string;
            [key: string]: any;
        };
        [key: string]: any;
    };
    [key: string]: any;
}
