export function limparPropriedadesHibernate<T>(obj: unknown): T {
  if (Array.isArray(obj)) {
    return obj.map((item) => limparPropriedadesHibernate(item)) as T;
  }

  if (obj !== null && typeof obj === 'object') {
    const result: any = {};
    for (const key in obj as Record<string, unknown>) {
      if (
        key === 'hibernateLazyInitializer' ||
        key === 'handler' ||
        key === 'byteBuddyInterceptor' ||
        key === 'ByteBuddyInterceptor'
      ) {
        continue;
      }

      result[key] = limparPropriedadesHibernate((obj as Record<string, unknown>)[key]);
    }
    return result;
  }

  return obj as T;
}