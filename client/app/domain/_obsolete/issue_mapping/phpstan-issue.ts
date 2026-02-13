export interface PhpstanIssue {
    message: string;
    line: number;
    ignorable: boolean;
    tip?: string;
    identifier: string;
}
