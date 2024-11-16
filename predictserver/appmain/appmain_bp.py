"""
1. activate predictserver
  > python.exe app.py
  
2. execute test program
  > python..exe post.py
"""
import ast
import io
import json
import random

import matplotlib
import matplotlib.pyplot as plt
import numpy as np
from flask import Blueprint, current_app, make_response, request
from PIL import Image
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing import image

matplotlib.use('Agg')

appmain_bp = Blueprint('appmain_bp', __name__,
                       template_folder='templates/appmain',
                       static_url_path='/appmain/static',
                       static_folder='../appmain/static',
                       )


def predict_image(input_data):
    # load model
    model_file = current_app.config["MODEL_PATH"]
    model = load_model(model_file)

    # predict image
    cuisine_dict = {0: "salad", 1: "sushi", 2: "tofu"}

    image_file = io.BytesIO(input_data)
    img = image.load_img(image_file, target_size=(150, 150))

    img_tensor = image.img_to_array(img)
    img_tensor = np.expand_dims(img_tensor, axis=0)
    img_tensor /= 255.

    p = model.predict(img_tensor)[0]
    answer = int(np.argmax(p))

    plt.title(cuisine_dict[answer], fontsize=12)
    plt.grid()
    plt.imshow(Image.open(image_file))

    save_name = io.BytesIO()
    plt.savefig(save_name)
    plt.close()

    return cuisine_dict[answer], save_name.getvalue()


@appmain_bp.route("/appmain", methods=["POST"])
def appmain():
    
    if list(request.form.values()):
        params = ast.literal_eval(list(request.form.values())[0])  # in case request form Spring Boot3
        print(f"[appmain_bp.py] {params= }")
    else:
        pass  # in case post.py test

    file = request.files["data_file"]
    file_name = file.filename
    print(f"[appmain_bp.py] {file_name= }")

    input_data = file.stream.read()
    answer, image_data = predict_image(input_data)
    print(f"[appmain_bp.py] {answer= }")

    with io.BytesIO(image_data) as f:
        response_data = f.read()

    response = make_response()

    response.headers["Results"] = json.dumps({"return_code": 0,
                                              "answer": answer,
                                              })

    # add 10 digits prefix in order to fix browser cash problem
    file_name = str(random.randrange(10**9, 10**10)) + "_answer.jpg"

    response.headers["Content-Type"] = "image/jpg"
    response.headers["Content-Disposition"] = f"attachment; filename={file_name}"
    response.data = response_data

    return response
