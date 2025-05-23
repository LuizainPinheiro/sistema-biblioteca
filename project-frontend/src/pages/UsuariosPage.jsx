// src/pages/UsuariosPage.jsx
import React, { useState, useEffect } from 'react';
import axios from 'axios';

function UsuariosPage() {
    const [usuarios, setUsuarios] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [mensagem, setMensagem] = useState(null);

    const [novoUsuario, setNovoUsuario] = useState({
        nome: '',
        email: '',
        telefone: '',
        endereco: '',
        dataRegistro: '', // Pode ser preenchido automaticamente no backend
        status: 'Ativo' // Valor padrão
    });

    const fetchUsuarios = async () => {
        try {
            setLoading(true);
            setError(null);
            const response = await axios.get('http://localhost:8080/api/usuarios');
            setUsuarios(response.data);
        } catch (err) {
            console.error("Erro ao buscar usuários:", err);
            setError(`Erro ao buscar usuários: ${err.response?.status} - ${err.response?.data?.message || 'Erro desconhecido'}`);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchUsuarios();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setNovoUsuario(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleAddUsuario = async (e) => {
        e.preventDefault();
        setMensagem(null);
        if (!novoUsuario.nome.trim() || !novoUsuario.email.trim() || !novoUsuario.telefone.trim() || !novoUsuario.endereco.trim()) {
            setMensagem('Todos os campos obrigatórios devem ser preenchidos.');
            return;
        }

        try {
            // O backend pode definir dataRegistro e status se não forem enviados
            const usuarioParaEnviar = {
                ...novoUsuario,
                dataRegistro: novoUsuario.dataRegistro || undefined // Envia undefined se vazio para o backend definir
            };
            await axios.post('http://localhost:8080/api/usuarios', usuarioParaEnviar);
            setMensagem('Usuário adicionado com sucesso!');
            setNovoUsuario({ nome: '', email: '', telefone: '', endereco: '', dataRegistro: '', status: 'Ativo' }); // Limpa o formulário
            fetchUsuarios(); // Atualiza a lista
        } catch (err) {
            console.error('Erro ao adicionar usuário:', err);
            setMensagem(`Erro ao adicionar usuário: ${err.response?.status} - ${err.response?.data?.message || 'Erro desconhecido'}`);
        }
    };

    const handleDeletarUsuario = async (id) => {
        if (window.confirm(`Tem certeza que deseja deletar o usuário com ID ${id}?`)) {
            try {
                await axios.delete(`http://localhost:8080/api/usuarios/${id}`);
                setMensagem('Usuário deletado com sucesso!');
                fetchUsuarios(); // Atualiza a lista
            } catch (err) {
                console.error("Erro ao deletar usuário:", err);
                setMensagem(`Erro ao deletar usuário: ${err.response?.status} - ${err.response?.data?.message || 'Erro desconhecido'}`);
            }
        }
    };

    if (loading) {
        return <p>Carregando lista de usuários...</p>;
    }

    if (error) {
        return <p style={{ color: 'red' }}>{error}</p>;
    }

    return (
        <main>
            <h2>Gerenciar Usuários</h2>

            {mensagem && (
                <p style={{ color: mensagem.includes('sucesso') ? 'green' : 'red' }}>
                    {mensagem}
                </p>
            )}

            <h3>Adicionar Novo Usuário</h3>
            <form onSubmit={handleAddUsuario}>
                <div>
                    <label htmlFor="nome">Nome:</label>
                    <input
                        type="text"
                        id="nome"
                        name="nome"
                        value={novoUsuario.nome}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="email">Email:</label>
                    <input
                        type="email"
                        id="email"
                        name="email"
                        value={novoUsuario.email}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="telefone">Telefone:</label>
                    <input
                        type="text"
                        id="telefone"
                        name="telefone"
                        value={novoUsuario.telefone}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="endereco">Endereço:</label>
                    <input
                        type="text"
                        id="endereco"
                        name="endereco"
                        value={novoUsuario.endereco}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="status">Status:</label>
                    <select
                        id="status"
                        name="status"
                        value={novoUsuario.status}
                        onChange={handleChange}
                        required
                    >
                        <option value="Ativo">Ativo</option>
                        <option value="Inativo">Inativo</option>
                        <option value="Bloqueado">Bloqueado</option>
                    </select>
                </div>
                <button type="submit">Adicionar Usuário</button>
            </form>

            <h3>Usuários Cadastrados</h3>
            {usuarios.length === 0 ? (
                <p>Nenhum usuário cadastrado.</p>
            ) : (
                <ul>
                    {usuarios.map(usuario => (
                        <li key={usuario.id}>
                            <strong>Nome:</strong> {usuario.nome} <br />
                            <strong>Email:</strong> {usuario.email} <br />
                            <strong>Telefone:</strong> {usuario.telefone} <br />
                            <strong>Endereço:</strong> {usuario.endereco} <br />
                            <strong>Registro:</strong> {new Date(usuario.dataRegistro).toLocaleDateString()} <br />
                            <strong>Status:</strong> {usuario.status} <br />
                            <button onClick={() => handleDeletarUsuario(usuario.id)}>Deletar</button>
                            {/* Adicionar botão de editar futuramente */}
                            <hr />
                        </li>
                    ))}
                </ul>
            )}
        </main>
    );
}

export default UsuariosPage;