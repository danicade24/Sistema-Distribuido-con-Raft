import socket
import threading
import os

MODELS_DIR = "../../models"

def handle_client(conn, addr):
    print(f"Conexión recibida de {addr}")
    try:
        data = conn.recv(1024).decode().strip()
        print(f"Mensaje recibido: {data}")

        if data.startswith("REPLICACION:"):
            model_id = data.split(":")[1]
            content = conn.recv(4096).decode()
            os.makedirs(MODELS_DIR, exist_ok=True)
            with open(f"{MODELS_DIR}/model_{model_id}.txt", "w") as f:
                f.write(content)
            conn.sendall(f"REPLICACION_OK:{model_id}\n".encode())
        else:
            conn.sendall("COMANDO_DESCONOCIDO\n".encode())

    except Exception as e:
        print(f"Error: {e}")
    finally:
        conn.close()

def start_server(host='0.0.0.0', port=6000):
    print(f"WorkerFollower escuchando en {host}:{port}")
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind((host, port))
        s.listen()
        while True:
            conn, addr = s.accept()
            threading.Thread(target=handle_client, args=(conn, addr)).start()

if __name__ == "__main__":
    start_server()
