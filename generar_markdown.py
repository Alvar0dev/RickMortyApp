import os

# Nombre del archivo markdown que se va a generar
OUTPUT_FILE = "proyecto_completo.md"

# Carpetas que no queremos incluir
IGNORED_DIRS = {".git", ".gradle", ".idea", "build", "gradle"}

# Archivos específicos que no queremos incluir
IGNORED_FILES = {
    ".gitignore", 
    "gradlew", 
    "gradlew.bat", 
    "generar_markdown.py", 
    "local.properties",
    OUTPUT_FILE
}

# Extensiones que sí queremos procesar (para no procesar imágenes o archivos binarios)
ALLOWED_EXTENSIONS = {
    ".xml", ".java", ".kt", ".kts", ".properties", ".pro", ".toml", ".gradle", ".json", ".md"
}

def get_language(extension):
    """Devuelve el lenguaje para el bloque de código de markdown dependiendo de la extensión."""
    ext_map = {
        ".xml": "xml",
        ".java": "java",
        ".kt": "kotlin",
        ".kts": "kotlin",
        ".properties": "properties",
        ".pro": "properties",
        ".toml": "toml",
        ".gradle": "groovy",
        ".json": "json",
        ".md": "markdown"
    }
    return ext_map.get(extension, "text")

def generate_markdown():
    # Obtiene la ruta donde está este script
    root_dir = os.path.dirname(os.path.abspath(__file__))
    output_path = os.path.join(root_dir, OUTPUT_FILE)
    
    with open(output_path, "w", encoding="utf-8") as out_file:
        out_file.write("# Código Fuente del Proyecto\n\n")
        
        # os.walk recorre todos los archivos y carpetas del directorio
        for root, dirs, files in os.walk(root_dir):
            
            # Filtra las carpetas: quita las que están en IGNORED_DIRS o contienen imágenes/recursos binarios comunes en Android
            dirs[:] = [
                d for d in dirs 
                if d not in IGNORED_DIRS 
                and not d.startswith("mipmap")
            ]
            
            for file in files:
                if file in IGNORED_FILES:
                    continue
                
                ext = os.path.splitext(file)[1].lower()
                if ext not in ALLOWED_EXTENSIONS:
                    continue
                
                # Ruta completa y ruta relativa (para mostrar en el título)
                file_path = os.path.join(root, file)
                rel_path = os.path.relpath(file_path, root_dir)
                
                # Ignorar la propia carpeta build si se coló alguna por dentro
                if "build" + os.sep in rel_path:
                    continue
                
                try:
                    with open(file_path, "r", encoding="utf-8") as f:
                        content = f.read()
                    
                    lang = get_language(ext)
                    out_file.write(f"## Archivo: `{rel_path}`\n\n")
                    out_file.write(f"```{lang}\n")
                    out_file.write(content)
                    if not content.endswith('\n'):
                        out_file.write("\n")
                    out_file.write("```\n\n")
                except Exception as e:
                    print(f"Error leyendo {rel_path}: {e}")
                    
    print(f"¡Markdown generado exitosamente en: {OUTPUT_FILE}!")

if __name__ == "__main__":
    generate_markdown()
