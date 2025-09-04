export function getStorage(key) {
    return localStorage.getItem(key)
}

export function setStorage(key, value) {
    let str = typeof (value) === 'string' ? value : JSON.stringify(value)
    return localStorage.setItem(key, str)
}

export function removeStorage(key) {
    localStorage.removeItem(key)
}