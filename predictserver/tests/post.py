import json
from pathlib import Path

import requests


def post(params, data_file, model_file):
    # REQUEST
    files = {"params": ("params", json.dumps(params), 'application/json'),
             "data_file": (data_file.name, open(data_file, "rb"),  "image/jpg"),
             "model_file": (model_file.name, open(model_file, "rb"), "application/octet-stream"),
             }
    print(f"{files= }")

    url = "http://localhost:8000/appmain"

    response = requests.post(url, files=files)

    # RESPONSE
    status_code = response.status_code
    print(f"\nstatus_code= {status_code}")
    
    content_type = response.headers["Content-Type"]
    content_disposition = response.headers["Content-Disposition"]
    content_length = response.headers["Content-Length"]

    print(f"Content-Type= {content_type}")
    print(f"Content-Disposition = {content_disposition}")
    print(f"Content-Length= {content_length}")

    # RESPONSE(parameter info.)
    Results = response.headers["Results"]  # str
    Results = json.loads(Results)  # json

    for k, v in Results.items():
        print("{}= {}".format(k, v))

    # RESPONSE(binary info.)
    ATTRIBUTE = 'filename='
    file_name = content_disposition[content_disposition.find(ATTRIBUTE) + len(ATTRIBUTE):]
    print(f"{file_name= }")

    if response.content:
        with open(Path("output", file_name), 'wb') as fw:
            fw.write(response.content)
    else:
        print(f"response.content=b''")

    return response


params = {"command": "predict"}
data_file = Path(r"./salad.jpg")
model_file = Path(r"../appmain/model/best_model_2.18.0.keras")

r = post(params, data_file, model_file)
print(r)
