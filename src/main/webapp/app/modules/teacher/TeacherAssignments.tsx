import React, { useEffect, useState } from 'react';
import {
  AssignmentDTO,
  createAssignment,
  CreateAssignmentDTO,
  deleteAssignment,
  getAllAssignments,
  updateAssignment,
} from 'app/services/teacher-assignment.service';
import './teacher-assignments.scss';
import { getTeacherClasses } from 'app/services/teacher/teacher-class.service';

export const TeacherAssignments: React.FC = () => {
  const [assignments, setAssignments] = useState<AssignmentDTO[]>([]);
  const [classRooms, setClassRooms] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [showModal, setShowModal] = useState(false);
  const [editingAssignment, setEditingAssignment] = useState<AssignmentDTO | null>(null);
  const [formData, setFormData] = useState<CreateAssignmentDTO>({
    classRoomId: 0,
    lessonId: 0,
    dueDate: '',
  });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      const [assignmentsData, classRoomsData] = await Promise.all([getAllAssignments(), getTeacherClasses()]);
      setAssignments(assignmentsData);
      setClassRooms(classRoomsData);
    } catch (err: any) {
      setError(err.message || 'Erro ao carregar dados');
    } finally {
      setLoading(false);
    }
  };

  const handleOpenModal = (assignment?: AssignmentDTO) => {
    if (assignment) {
      setEditingAssignment(assignment);
      setFormData({
        classRoomId: assignment.classRoomId,
        lessonId: assignment.lessonId,
        dueDate: assignment.dueDate.split('T')[0],
      });
    } else {
      setEditingAssignment(null);
      setFormData({
        classRoomId: 0,
        lessonId: 0,
        dueDate: '',
      });
    }
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setEditingAssignment(null);
    setFormData({
      classRoomId: 0,
      lessonId: 0,
      dueDate: '',
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const dueDateISO = new Date(formData.dueDate).toISOString();
      const payload = { ...formData, dueDate: dueDateISO };

      if (editingAssignment) {
        await updateAssignment(editingAssignment.id, payload);
      } else {
        await createAssignment(payload);
      }
      await loadData();
      handleCloseModal();
    } catch (err: any) {
      setError(err.message || 'Erro ao salvar atribuição');
    }
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Tem certeza que deseja excluir esta atribuição?')) {
      try {
        await deleteAssignment(id);
        await loadData();
      } catch (err: any) {
        setError(err.message || 'Erro ao excluir atribuição');
      }
    }
  };

  if (loading) {
    return (
      <div className="teacher-assignments">
        <div className="loading">Carregando...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="teacher-assignments">
        <div className="error">{error}</div>
      </div>
    );
  }

  return (
    <div className="teacher-assignments">
      <div className="header">
        <h1>Atribuições de Lições</h1>
        <button className="btn-primary" onClick={() => handleOpenModal()}>
          Nova Atribuição
        </button>
      </div>

      <div className="assignments-list">
        {assignments.length === 0 ? (
          <div className="empty-state">
            <p>Nenhuma atribuição encontrada.</p>
          </div>
        ) : (
          <table className="assignments-table">
            <thead>
              <tr>
                <th>Turma</th>
                <th>Lição</th>
                <th>Data de Atribuição</th>
                <th>Prazo</th>
                <th>Atribuído por</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {assignments.map(assignment => (
                <tr key={assignment.id}>
                  <td>{assignment.classRoomName}</td>
                  <td>{assignment.lessonTitle}</td>
                  <td>{new Date(assignment.assignedAt).toLocaleDateString('pt-BR')}</td>
                  <td>{new Date(assignment.dueDate).toLocaleDateString('pt-BR')}</td>
                  <td>{assignment.assignedByLogin}</td>
                  <td>
                    <button className="btn-edit" onClick={() => handleOpenModal(assignment)}>
                      Editar
                    </button>
                    <button className="btn-delete" onClick={() => handleDelete(assignment.id)}>
                      Excluir
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {showModal && (
        <div className="modal-overlay" onClick={handleCloseModal}>
          <div className="modal-content" onClick={e => e.stopPropagation()}>
            <h2>{editingAssignment ? 'Editar Atribuição' : 'Nova Atribuição'}</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label htmlFor="classRoomId">Turma</label>
                <select
                  id="classRoomId"
                  value={formData.classRoomId}
                  onChange={e => setFormData({ ...formData, classRoomId: Number(e.target.value) })}
                  required
                >
                  <option value={0}>Selecione uma turma</option>
                  {classRooms.map(classRoom => (
                    <option key={classRoom.id} value={classRoom.id}>
                      {classRoom.name}
                    </option>
                  ))}
                </select>
              </div>

              <div className="form-group">
                <label htmlFor="lessonId">Lição (ID)</label>
                <input
                  type="number"
                  id="lessonId"
                  value={formData.lessonId || ''}
                  onChange={e => setFormData({ ...formData, lessonId: Number(e.target.value) })}
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="dueDate">Prazo</label>
                <input
                  type="date"
                  id="dueDate"
                  value={formData.dueDate}
                  onChange={e => setFormData({ ...formData, dueDate: e.target.value })}
                  required
                />
              </div>

              <div className="modal-actions">
                <button type="button" className="btn-secondary" onClick={handleCloseModal}>
                  Cancelar
                </button>
                <button type="submit" className="btn-primary">
                  {editingAssignment ? 'Atualizar' : 'Criar'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
