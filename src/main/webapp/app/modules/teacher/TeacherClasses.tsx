import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import { getTeacherClasses, ClassRoom } from 'app/services/teacher/teacher-class.service';
import './teacher-classes.scss';

export const TeacherClasses = () => {
  const [classes, setClasses] = useState<ClassRoom[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadClasses();
  }, []);

  const loadClasses = async () => {
    try {
      setLoading(true);
      const data = await getTeacherClasses();
      setClasses(data);
    } catch (error) {
      toast.error('Erro ao carregar turmas');
      console.error('Error loading classes:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="teacher-classes">
        <div className="loading-skeleton">
          <div className="skeleton-card"></div>
          <div className="skeleton-card"></div>
          <div className="skeleton-card"></div>
        </div>
      </div>
    );
  }

  return (
    <div className="teacher-classes">
      <div className="classes-header">
        <h1>Minhas Turmas</h1>
        <Link to="/class-room/new" className="btn-primary">
          + Nova Turma
        </Link>
      </div>

      {classes.length === 0 ? (
        <div className="empty-state">
          <div className="empty-icon">🏫</div>
          <h2>Nenhuma turma criada</h2>
          <p>Crie sua primeira turma para começar a gerenciar alunos e atribuir lições</p>
          <Link to="/class-room/new" className="btn-primary">
            Criar primeira turma
          </Link>
        </div>
      ) : (
        <div className="classes-grid">
          {classes.map(classRoom => (
            <Link key={classRoom.id} to={`/teacher/classes/${classRoom.id}`} className="class-card">
              <div className="class-header">
                <h3>{classRoom.name}</h3>
                <span className="class-language">{classRoom.language}</span>
              </div>
              <p className="class-description">{classRoom.description || 'Sem descrição'}</p>
              <div className="class-footer">
                <span className="class-date">Criada em {new Date(classRoom.createdAt).toLocaleDateString('pt-BR')}</span>
              </div>
            </Link>
          ))}
        </div>
      )}
    </div>
  );
};

export default TeacherClasses;
