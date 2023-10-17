import type { ObjectDirective } from "vue";

const Closable = (onMount = false): ObjectDirective => ({
    mounted(el, binding) {
        el.handleClickOutside = (event:Event) => {
            event.stopPropagation();
            const { handler, excluded } = binding.value;
            let clickedOnExcludedEl = false;
            // Exclude elements passed as an Array
            if (excluded) {
                excluded.forEach((refName: any) => {
                    if (!clickedOnExcludedEl) {
                        const excludedEl = document.getElementsByClassName(refName)[0];
                        clickedOnExcludedEl = excludedEl.contains(event.target as Node);
                    }
                });
            }
            if (!el.contains(event.target) && !clickedOnExcludedEl) {
                handler(event);
            }
        }
        if(onMount) {
            document.addEventListener('mouseup', el.handleClickOutside);
        }
    },
    updated(el) {
        if(!onMount) {
            if (el.style.display !== 'none') {
                document.addEventListener('mouseup', el.handleClickOutside);
            } else {
                document.removeEventListener('mouseup', el.handleClickOutside);
            }
        }
    },
    unmounted(el) {
        document.removeEventListener('mouseup', el.handleClickOutside);
    }
});

export default Closable;