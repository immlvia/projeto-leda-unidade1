import numpy as np
import matplotlib.pyplot as plt

# Configurações de estilo
plt.rcParams['font.sans-serif'] = 'DejaVu Sans'
plt.rcParams['axes.edgecolor'] = '#cccccc'
plt.rcParams['axes.linewidth'] = 1.0


labels_x = ['10.000', '50.000', '100.000']
x = np.arange(len(labels_x))

bin_iterativa = [19.71, 20.11, 22.23]
bin_recursiva = [29.60, 32.18, 32.80]

lin_iterativa = [2063.73, 9745.78, 17982.80]
lin_duas_pontas = [4097.63, 17821.30, 35430.23]
lin_recursiva = [47482.10, 241003.43, 480704.90]


# Todos os Algoritmos (Escala Log)

fig, ax = plt.subplots(figsize=(10, 5.5), dpi=200)
width = 0.15

ax.bar(x - 2*width, bin_iterativa,   width, label='Busca Binária Iterativa',
       color='#1266b5', edgecolor='white', linewidth=0.7, zorder=3)
ax.bar(x - width,   bin_recursiva,   width, label='Busca Binária Recursiva',
       color='#267a29', edgecolor='white', linewidth=0.7, zorder=3)
ax.bar(x,           lin_iterativa,   width, label='Busca Linear Iterativa',
       color='#ef7f1a', edgecolor='white', linewidth=0.7, zorder=3)
ax.bar(x + width,   lin_duas_pontas, width, label='Busca Linear Duas Pontas',
       color='#621887', edgecolor='white', linewidth=0.7, zorder=3)
ax.bar(x + 2*width, lin_recursiva,   width, label='Busca Linear Recursiva',
       color='#b81d18', edgecolor='white', linewidth=0.7, zorder=3)

ax.set_yscale('log')
ax.set_title('Desempenho dos Algoritmos de Busca O(log n) e O(n) (alvo = -1)',
             fontsize=13, fontweight='bold', pad=12)
ax.set_xlabel('Tamanho do vetor (n)', fontsize=11)
ax.set_ylabel('Tempo médio (ns/busca) – Escala Log', fontsize=11)
ax.set_xticks(x)
ax.set_xticklabels(labels_x, fontsize=10.5)
ax.grid(axis='y', linestyle='--', color='#dddddd', alpha=0.8, zorder=0)
ax.set_ylim(10, 1.5e6)
ax.legend(loc='upper left', frameon=True, edgecolor='#cccccc', fontsize=10)

plt.tight_layout()
plt.savefig('todos_algoritmos_busca_log.png', dpi=200)
plt.show()

# Algoritmos de Busca Binária O(log n)

fig1, ax1 = plt.subplots(figsize=(9, 5), dpi=200)
width1 = 0.25

ax1.bar(x - width1/2, bin_iterativa, width1, label='Busca Binária Iterativa',
        color='#1266b5', edgecolor='white', linewidth=0.7, zorder=3)
ax1.bar(x + width1/2, bin_recursiva, width1, label='Busca Binária Recursiva',
        color='#267a29', edgecolor='white', linewidth=0.7, zorder=3)

ax1.set_title('Algoritmos de Busca Binária O(log n) (alvo = -1)',
              fontsize=13, fontweight='bold', pad=12)
ax1.set_xlabel('Tamanho do vetor (n)', fontsize=11)
ax1.set_ylabel('Tempo médio (ns/busca)', fontsize=11)
ax1.set_xticks(x)
ax1.set_xticklabels(labels_x, fontsize=10.5)
ax1.grid(axis='y', linestyle='--', color='#dddddd', alpha=0.8, zorder=0)
ax1.set_ylim(0, max(bin_recursiva) * 1.18)
ax1.legend(loc='upper left', frameon=True, edgecolor='#cccccc', fontsize=10)

plt.tight_layout()
plt.savefig('grafico1_busca_binaria.png', dpi=200)
plt.show()


# Algoritmos de Busca Linear O(n)

fig2, ax2 = plt.subplots(figsize=(9, 5), dpi=200)
width2 = 0.22

ax2.bar(x - width2, lin_iterativa, width2, label='Busca Linear Iterativa',
        color='#1266b5', edgecolor='white', linewidth=0.7, zorder=3)
ax2.bar(x,          lin_duas_pontas, width2, label='Busca Linear Duas Pontas',
        color='#267a29', edgecolor='white', linewidth=0.7, zorder=3)
ax2.bar(x + width2, lin_recursiva, width2, label='Busca Linear Recursiva',
        color='#b81d18', edgecolor='white', linewidth=0.7, zorder=3)

ax2.set_title('Algoritmos de Busca Linear O(n) (alvo = -1)',
              fontsize=13, fontweight='bold', pad=12)
ax2.set_xlabel('Tamanho do vetor (n)', fontsize=11)
ax2.set_ylabel('Tempo médio (ns/busca)', fontsize=11)
ax2.set_xticks(x)
ax2.set_xticklabels(labels_x, fontsize=10.5)
ax2.grid(axis='y', linestyle='--', color='#dddddd', alpha=0.8, zorder=0)
ax2.set_ylim(0, max(lin_recursiva) * 1.15)
ax2.legend(loc='upper left', frameon=True, edgecolor='#cccccc', fontsize=10)

plt.tight_layout()
plt.savefig('grafico2_busca_linear.png', dpi=200)
plt.show()

# Busca Linear Iterativa vs. Duas Pontas

fig3, ax3 = plt.subplots(figsize=(9, 5), dpi=200)

ax3.bar(x - width1/2, lin_iterativa, width1, label='Busca Linear Iterativa',
        color='#1266b5', edgecolor='white', linewidth=0.7, zorder=3)
ax3.bar(x + width1/2, lin_duas_pontas, width1, label='Busca Linear Duas Pontas',
        color='#e67e00', edgecolor='white', linewidth=0.7, zorder=3)

ax3.set_title('Busca Linear Iterativa vs. Duas Pontas O(n) (alvo = -1)',
              fontsize=13, fontweight='bold', pad=12)
ax3.set_xlabel('Tamanho do vetor (n)', fontsize=11)
ax3.set_ylabel('Tempo médio (ns/busca)', fontsize=11)
ax3.set_xticks(x)
ax3.set_xticklabels(labels_x, fontsize=10.5)
ax3.grid(axis='y', linestyle='--', color='#dddddd', alpha=0.8, zorder=0)
ax3.set_ylim(0, max(lin_duas_pontas) * 1.15)
ax3.legend(loc='upper left', frameon=True, edgecolor='#cccccc', fontsize=10)

plt.tight_layout()
plt.savefig('grafico3_linear_sem_recursiva.png', dpi=200)
plt.show()
