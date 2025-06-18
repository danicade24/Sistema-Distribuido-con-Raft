import socket
import time
import threading

HOST = 'localhost'
PORT = 5000
TOTAL = 100
CONCURRENCIA = 20  # número de hilos simultáneos

def enviar_entrenamiento(i):
    try:
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect((HOST, PORT))
            s.sendall(b'ENTRENAMIENTO\n')
            respuesta = s.recv(1024).decode().strip()
            print(f"[{i:04d}] Respuesta: {respuesta}")
    except Exception as e:
        print(f"[{i:04d}] Error: {e}")

def main():
    inicio = time.time()
    hilos = []

    for i in range(TOTAL):
        t = threading.Thread(target=enviar_entrenamiento, args=(i,))
        t.start()
        hilos.append(t)

        # Controlar ráfaga de hilos
        if i % CONCURRENCIA == 0:
            time.sleep(0.1)

    for t in hilos:
        t.join()

    fin = time.time()
    print(f"\nTiempo total: {fin - inicio:.2f} segundos")

if __name__ == "__main__":
    main()
