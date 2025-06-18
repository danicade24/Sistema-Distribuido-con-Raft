import socket
import threading
import os
import sys

PORT = 6000  # puerto que debe coincidir con FOLLOWERS en Java
MODEL_DIR = f"models_{PORT}"  # cada follower tiene su propia carpeta

os.makedirs(MODEL_DIR, exist_ok=True)

def handle_client(conn, addr):
    print(f"Conexión de {addr}")
    try:
        data = conn.recv(4096).decode().strip()
        print(f"Mensaje recibido: {data}")

        if data.startswith("REPLICA:"):
            try:
                model_id, contenido = data[8:].split(";", 1)
                model_id = model_id.strip()
                contenido = contenido.replace("\\n", "\n")

                file_path = os.path.join(MODEL_DIR, f"model_{model_id}.txt")
                with open(file_path, "w") as f:
                    f.write(contenido)

                print(f"Modelo replicado y guardado: {file_path}")
                sys.stdout.flush()
                conn.sendall(b"REPLICA_OK\n")
            except Exception as e:
                print(f"Error procesando REPLICA: {e}")
                conn.sendall(b"REPLICA_FAIL\n")
        else:
            conn.sendall(b"COMANDO_DESCONOCIDO\n")

    except Exception as e:
        print(f"Error general: {e}")
    finally:
        conn.close()
    

def start_server(host='localhost', port=PORT):
    print(f"Worker follower Python escuchando en {host}:{port}")
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind((host, port))
        s.listen()
        while True:
            conn, addr = s.accept()
            threading.Thread(target=handle_client, args=(conn, addr)).start()

if __name__ == "__main__":
    start_server()
