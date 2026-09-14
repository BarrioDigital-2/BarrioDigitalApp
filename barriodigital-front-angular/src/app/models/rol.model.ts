export const ROLES = {
  ADMIN: 'Admin',
  FUNCIONARIO: 'Funcionario',
  VECINO: 'Vecino',
  AUDITOR: 'Auditor',
} as const;

export type Rol = (typeof ROLES)[keyof typeof ROLES];