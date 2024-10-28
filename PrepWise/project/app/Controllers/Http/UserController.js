'use strict'

class UserController {
  async index({ response }) {
    // Логіка для отримання всіх користувачів
    return response.json({ message: 'Список користувачів' });
  }

  async store({ request, response }) {
    // Логіка для створення нового користувача
    const data = request.only(['name', 'email']);
    return response.json({ message: 'Користувач створений', data });
  }

  async show({ params, response }) {
    // Логіка для отримання одного користувача
    return response.json({ message: `Отримати користувача з ID ${params.id}` });
  }

  async update({ params, request, response }) {
    // Логіка для оновлення користувача
    return response.json({ message: `Оновити користувача з ID ${params.id}` });
  }

  async destroy({ params, response }) {
    // Логіка для видалення користувача
    return response.json({ message: `Видалити користувача з ID ${params.id}` });
  }
}

module.exports = UserController;
