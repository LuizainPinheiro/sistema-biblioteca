// src/App.jsx
import { Routes, Route, Link } from 'react-router-dom';

import HomePage from './pages/HomePage';
import LivrosPage from './pages/LivrosPage';
import UsuariosPage from './pages/UsuariosPage';
import EmprestimosPage from './pages/EmprestimosPage';
import LivroForm from './components/LivroForm';
import AutoresPage from './pages/AutoresPage';       // Importa a nova página de Autores
import CategoriasPage from './pages/CategoriasPage'; // Importa a nova página de Categorias

function App() {
  return (
    <div>
      <h1>Biblioteca Digital</h1>

      <nav>
        <ul>
          <li>
            <Link to="/">Home</Link>
          </li>
          <li>
            <Link to="/livros">Livros</Link>
          </li>
          <li>
            <Link to="/livros/novo">Adicionar Livro</Link>
          </li>
          <li>
            <Link to="/autores">Autores</Link> {/* Novo link para a página de Autores */}
          </li>
          <li>
            <Link to="/categorias">Categorias</Link> {/* Novo link para a página de Categorias */}
          </li>
          <li>
            <Link to="/usuarios">Usuários</Link>
          </li>
          <li>
            <Link to="/emprestimos">Empréstimos</Link>
          </li>
        </ul>
      </nav>

      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/livros" element={<LivrosPage />} />
        <Route path="/livros/novo" element={<LivroForm />} />
        <Route path="/autores" element={<AutoresPage />} />       {}
        <Route path="/categorias" element={<CategoriasPage />} /> {}
        <Route path="/usuarios" element={<UsuariosPage />} />
        <Route path="/emprestimos" element={<EmprestimosPage />} />
        <Route path="*" element={<h2>Página Não Encontrada</h2>} />
      </Routes>
    </div>
  );
}

export default App;