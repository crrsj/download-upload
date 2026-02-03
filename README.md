🚀 FileStream API: Upload, Download & Management
O FileStream API é um serviço backend desenvolvido em Java com Spring Boot focado na manipulação eficiente de arquivos. A API permite o armazenamento seguro de documentos, a recuperação através de download e a listagem dinâmica de arquivos presentes no servidor.

📋 Funcionalidades
Upload de Arquivos: Recebimento de arquivos via MultipartFile com validação de extensão e tamanho.

Download Seguro: Recuperação de arquivos através de Resource do Spring, garantindo o streaming correto dos dados.

Listagem de Arquivos: Endpoint que retorna os metadados (nome, tamanho, link de acesso) de todos os arquivos armazenados.

Gerenciamento de Storage: Lógica para criação automática de diretórios e tratamento de nomes duplicados.

🛠️ Stack Técnica
Linguagem: Java 17+

Framework: Spring Boot 3.x

Storage: File System (Local Storage)
