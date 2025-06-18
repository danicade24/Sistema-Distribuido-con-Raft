from http.server import BaseHTTPRequestHandler, HTTPServer
import os

BASE_DIR = os.path.dirname(os.path.abspath(__file__))  # esto da: python/worker_follower
MODELS_DIR = os.path.abspath(os.path.join(BASE_DIR, "../../models"))
PORT = 8080

class ModelMonitorHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        if self.path == '/':
            self.send_response(200)
            self.send_header("Content-type", "text/html")
            self.end_headers()

            html = "<html><head><title>Modelos Replicados</title></head><body>"
            html += "<h2>Modelos disponibles en el follower</h2><ul>"

            try:
                for fname in os.listdir(MODELS_DIR):
                    full_path = os.path.join(MODELS_DIR, fname)
                    if not os.path.isfile(full_path): continue

                    with open(full_path, 'r') as f:
                        content = f.read()
                    html += f"<li><strong>{fname}</strong><pre>{content}</pre></li><br>"
            except Exception as e:
                html += f"<p><b>Error:</b> {e}</p>"

            html += "</ul></body></html>"
            self.wfile.write(html.encode())
        else:
            self.send_error(404)

def run_server():
    print(f"Servidor HTTP corriendo en http://localhost:{PORT}/")
    # os.chdir(".")  # en caso MODELS_DIR esté fuera del scope directo
    server_address = ('', PORT)
    httpd = HTTPServer(server_address, ModelMonitorHandler)
    httpd.serve_forever()

if __name__ == '__main__':
    run_server()
