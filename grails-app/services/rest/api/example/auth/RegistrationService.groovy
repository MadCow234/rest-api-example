package rest.api.example.auth

import grails.gorm.transactions.Transactional

@Transactional
class RegistrationService {

    def registerNewUser(String username, String password) {
        log.info("Registering new user. Username: $username")

        def user = new User()
        user.username = username
        user.password = password
        user.passwordExpired = false
        user.accountLocked = false
        user.accountExpired = false
        user.enabled = true

        if (!user.save(flush: true)) {
            log.error("Could not register user! Reason: $user.errors")
            return user

        } else {
            Role role = Role.findByAuthority("ROLE_USER")

            UserRole userRole = new UserRole()
            userRole.user = user
            userRole.role = role

            if (!userRole.save(flush: true)) {
                log.error("Could not assign $role.authority to $user.username. Reason: $user.errors")
                return user
            }
        }

        log.info("Successfully created user: $user.username.")
        return user
    }
}
