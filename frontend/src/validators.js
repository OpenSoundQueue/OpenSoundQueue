import {helpers, email, minLength, sameAs, required} from '@vuelidate/validators';

export const required$ = helpers.withMessage('Dieses Feld darf nicht leer bleiben', required)
export const email$ = helpers.withMessage('Keine gültige E-Mail-Adresse', email)
export const passwordMinLength$ = (min) => helpers.withMessage(({$params}) => `Das Passwort muss mindestens ${$params.min} Zeichen lang sein`, minLength(min))
export const passwordSameAs$ = (equalTo) => helpers.withMessage('Passwörter müssen übereinstimmen', sameAs(equalTo))
export const usernameMinLength$ = (min) => helpers.withMessage(({$params}) => `Der Benutzername muss mindestens ${$params.min} Zeichen lang sein`, minLength(min))
