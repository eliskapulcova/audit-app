import csv
import json
import os
import re

CSV_FILE = '2026-01-05-age-verification-issues-report.csv'
JSON_FILE = 'issues-report.json'
TS_FILE = 'issues-report.ts'

def parse_value(value):
    # Try to parse boolean
    if value.lower() == 'true':
        return True
    if value.lower() == 'false':
        return False
    
    # Try to parse number (float or int)
    try:
        f = float(value)
        if f.is_integer():
            return int(f)
        return f
    except ValueError:
        pass
    
    return value

def infer_ts_type(val):
    if isinstance(val, bool):
        return 'boolean'
    if isinstance(val, (int, float)):
        return 'number'
    return 'string'

def main():
    # Detect delimiter
    with open(CSV_FILE, 'r', encoding='utf-8') as f:
        first_line = f.readline()
        if '\t' in first_line:
            delimiter = '\t'
        else:
            delimiter = ','
            
    print(f"Detected delimiter: {repr(delimiter)}")

    data = []
    with open(CSV_FILE, 'r', encoding='utf-8') as f:
        reader = csv.DictReader(f, delimiter=delimiter)
        for row in reader:
            parsed_row = {k: parse_value(v) for k, v in row.items()}
            data.append(parsed_row)
            
    # Write JSON
    with open(JSON_FILE, 'w', encoding='utf-8') as f:
        json.dump(data, f, indent=2)
    print(f"Generated {JSON_FILE}")

    # Generate TypeScript definitions
    if not data:
        print("No data found to generate TypeScript definitions.")
        return

    # Infer types from the first row (assuming homogeneity)
    # Better approach: check all rows to see if any field becomes string
    
    field_types = {}
    
    for row in data:
        for k, v in row.items():
            current_type = infer_ts_type(v)
            if k not in field_types:
                field_types[k] = current_type
            else:
                if field_types[k] != current_type:
                    # If mixed, fallback to string or any? 
                    # Usually if we have boolean and string, it's string or boolean.
                    # If number and string, string.
                    # If boolean and number, maybe union or any.
                    # For simplicity, if conflict, default to string unless it's null handling which we skipped.
                    # Let's simple fallback to string if conflict for now, or union.
                    if field_types[k] == 'number' and current_type == 'boolean':
                         field_types[k] = 'number | boolean'
                    elif field_types[k] != current_type:
                         field_types[k] = 'string'

    ts_content = "export interface Issue {\n"
    for k, v in field_types.items():
        # Handle valid JS identifiers for keys
        if not re.match(r'^[a-zA-Z_$][a-zA-Z0-9_$]*$', k):
            key_str = f"'{k}'"
        else:
            key_str = k
        ts_content += f"  {key_str}: {v};\n"
    ts_content += "}\n\n"
    
    ts_content += "export const issues: Issue[] = " + json.dumps(data, indent=2) + ";\n"

    with open(TS_FILE, 'w', encoding='utf-8') as f:
        f.write(ts_content)
    print(f"Generated {TS_FILE}")

if __name__ == '__main__':
    main()
