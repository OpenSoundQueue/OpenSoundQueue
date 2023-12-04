export default [
    {
        "id": 0,
        "name": "Admin",
        "permissions": [
            {
                "skip": true
            },
            {
                "play": true
            },
            {
                "viewAdminPanel": true
            }
        ]
    },
    {
        "id": 1,
        "name": "Mod",
        "permissions": [
            {
                "skip": true
            },
            {
                "play": true
            },
            {
                "viewAdminPanel": false
            }
        ]
    },
    {
        "id": 2,
        "name": "default",
        "permissions": [
            {
                "skip": false
            },
            {
                "play": false
            },
            {
                "viewAdminPanel": false
            }
        ]
    }
]