from flask import Flask
from appmain.appmain_bp import appmain_bp


def create_app():
    app = Flask(__name__)
    # set context
    with app.app_context():
        app.config.from_object("settings")

    # register Blueprint
    app.register_blueprint(appmain_bp)

    return app


app = create_app()  # factory


if __name__ == "__main__":
    app.run(host="127.0.0.1", port=8000, threaded=False, debug=True)
