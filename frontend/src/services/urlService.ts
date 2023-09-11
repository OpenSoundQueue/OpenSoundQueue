export function resolveFilePath(filename: string)
{
    return new URL("../assets/" + filename, import.meta.url).href;
}