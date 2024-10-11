import json

# 1. JSON 파일 읽기
with open("./data/detailLocation.json", "r", encoding="utf-8") as file:
    file_data = json.load(file)

# 2. json_to_toggle 함수 정의
def json_to_toggle(data, indent=2):
    html = ""
    for key, value in data.items():
        if isinstance(value, dict):
            html += f"<details style='margin-left:{indent * 10}px'><summary>{key}</summary>{json_to_toggle(value, indent + 1)}</details>"
        elif isinstance(value, list):
            html += f"<details style='margin-left:{indent * 10}px'><summary>{key} (list)</summary>"
            for item in value:
                if isinstance(item, dict):
                    html += f"<details style='margin-left:{(indent + 1) * 10}px'><summary>Item</summary>{json_to_toggle(item, indent + 2)}</details>"
                else:
                    html += f"<div style='margin-left:{(indent + 1) * 10}px'>{item}</div>"
            html += "</details>"
        else:
            html += f"<div style='margin-left:{indent * 10}px'>{key}: {value}</div>"
    return html

# 3. HTML 코드 생성
toggle_html_from_file = f"<details open><summary>googleMap.json Data</summary>{json_to_toggle(file_data)}</details>"

# 4. HTML을 파일로 저장
html_file_path = "./data/googleMap.html"
with open(html_file_path, "w", encoding="utf-8") as html_file:
    html_file.write(toggle_html_from_file)

print(f"HTML 파일이 생성되었습니다: {html_file_path}")