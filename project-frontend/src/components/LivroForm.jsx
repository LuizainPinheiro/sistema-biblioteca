// src/components/LivroForm.jsx (Revisado para sua nova estrutura)
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

// Removida a prop onLivroAdicionado, pois a navegação já atualiza a lista
function LivroForm() {
    const navigate = useNavigate();
    const [livro, setLivro] = useState({
        titulo: '',
        isbn: '',
        anoPublicacao: '',
        editora: '',
        genero: '',
        numeroCopias: '',
    });

    const [selectedAutoresIds, setSelectedAutoresIds] = useState([]);
    const [selectedCategoriaId, setSelectedCategoriaId] = useState('');

    const [autoresDisponiveis, setAutoresDisponiveis] = useState([]);
    const [categoriasDisponiveis, setCategoriasDisponiveis] = useState([]);

    const [mensagem, setMensagem] = useState(null);

    useEffect(() => {
        const fetchAutoresAndCategorias = async () => {
            try {
                const autoresResponse = await axios.get('http://localhost:8080/api/autores');
                setAutoresDisponiveis(autoresResponse.data);

                const categoriasResponse = await axios.get('http://localhost:8080/api/categorias');
                setCategoriasDisponiveis(categoriasResponse.data);
            } catch (error) {
                console.error('Erro ao carregar autores ou categorias:', error);
                setMensagem('Erro ao carregar opções de autores/categorias. Verifique o console.');
            }
        };
        fetchAutoresAndCategorias();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setLivro(prevLivro => ({
            ...prevLivro,
            [name]: value
        }));
    };

    const handleAutoresChange = (e) => {
        const options = Array.from(e.target.selectedOptions);
        const values = options.map(option => parseInt(option.value));
        setSelectedAutoresIds(values);
    };

    const handleCategoriaChange = (e) => {
        setSelectedCategoriaId(parseInt(e.target.value));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMensagem(null);

        try {
            const anoPublicacaoNumerico = livro.anoPublicacao ? parseInt(livro.anoPublicacao) : null;
            const numeroCopiasNumerico = livro.numeroCopias ? parseInt(livro.numeroCopias) : null;
            
            const categoriaIdFinal = selectedCategoriaId || null; // Garante que seja null se não selecionado
            const autoresIdsFinal = selectedAutoresIds.length > 0 ? selectedAutoresIds : [];

            const livroParaEnviar = {
                titulo: livro.titulo,
                isbn: livro.isbn,
                editora: livro.editora,
                genero: livro.genero,
                anoPublicacao: isNaN(anoPublicacaoNumerico) ? null : anoPublicacaoNumerico,
                numeroCopias: isNaN(numeroCopiasNumerico) ? null : numeroCopiasNumerico,
                autoresIds: autoresIdsFinal,
                categoriaId: categoriaIdFinal,
            };

            console.log("Enviando LivroRequestDTO:", livroParaEnviar);

            const response = await axios.post('http://localhost:8080/api/livros', livroParaEnviar);
            setMensagem('Livro adicionado com sucesso!');
            console.log('Livro adicionado:', response.data);

            // Navega para a página de livros após o sucesso
            navigate('/livros');

            // Limpa o formulário (opcional, já que vai navegar)
            setLivro({
                titulo: '',
                isbn: '',
                anoPublicacao: '',
                editora: '',
                genero: '',
                numeroCopias: '',
            });
            setSelectedAutoresIds([]);
            setSelectedCategoriaId('');

        } catch (err) {
            console.error('Erro ao adicionar livro:', err);
            if (err.response) {
                const apiErrorMessage = err.response.data.message || JSON.stringify(err.response.data);
                setMensagem(`Erro ao adicionar livro: ${err.response.status} - ${apiErrorMessage}`);
            } else if (err.request) {
                setMensagem('Erro de rede: O back-end não está respondendo. Verifique se ele está rodando na porta 8080.');
            } else {
                setMensagem(`Erro inesperado: ${err.message}`);
            }
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h3>Adicionar Novo Livro</h3>
            {mensagem && (
                <p style={{ color: mensagem.includes('sucesso') ? 'green' : 'red' }}>
                    {mensagem}
                </p>
            )}
            <div>
                <label htmlFor="titulo">Título:</label>
                <input
                    type="text"
                    id="titulo"
                    name="titulo"
                    value={livro.titulo}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label htmlFor="autores">Autores:</label>
                <select
                    id="autores"
                    name="autores"
                    multiple
                    value={selectedAutoresIds}
                    onChange={handleAutoresChange}
                    required
                >
                    {autoresDisponiveis.map(autor => (
                        <option key={autor.id} value={autor.id}>
                            {autor.nome}
                        </option>
                    ))}
                </select>
            </div>
            <div>
                <label htmlFor="isbn">ISBN:</label>
                <input
                    type="text"
                    id="isbn"
                    name="isbn"
                    value={livro.isbn}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label htmlFor="genero">Gênero:</label>
                <input
                    type="text"
                    id="genero"
                    name="genero"
                    value={livro.genero}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label htmlFor="anoPublicacao">Ano de Publicação:</label>
                <input
                    type="number"
                    id="anoPublicacao"
                    name="anoPublicacao"
                    value={livro.anoPublicacao}
                    onChange={handleChange}
                />
            </div>
            <div>
                <label htmlFor="editora">Editora:</label>
                <input
                    type="text"
                    id="editora"
                    name="editora"
                    value={livro.editora}
                    onChange={handleChange}
                />
            </div>
            <div>
                <label htmlFor="numeroCopias">Número de Cópias:</label>
                <input
                    type="number"
                    id="numeroCopias"
                    name="numeroCopias"
                    value={livro.numeroCopias}
                    onChange={handleChange}
                />
            </div>
            <div>
                <label htmlFor="categoria">Categoria:</label>
                <select
                    id="categoria"
                    name="categoria"
                    value={selectedCategoriaId}
                    onChange={handleCategoriaChange}
                    required
                >
                    <option value="">Selecione uma categoria</option>
                    {categoriasDisponiveis.map(categoria => (
                        <option key={categoria.id} value={categoria.id}>
                            {categoria.nome}
                        </option>
                    ))}
                </select>
            </div>
            <button type="submit">Adicionar Livro</button>
        </form>
    );
}

export default LivroForm;