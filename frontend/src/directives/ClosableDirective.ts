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

            // If clicked outside the element and not on an excluded element, call the handler
            if (!el.contains(event.target) && !clickedOnExcludedEl) {
                handler(event);
            }
        }

        // Attach event listener on mount if specified
        if(onMount) {
            document.addEventListener('mouseup', el.handleClickOutside);
        }
    },
    updated(el) {
        // Update event listener based on element's display style
        if(!onMount) {
            if (el.style.display !== 'none') {
                document.addEventListener('mouseup', el.handleClickOutside);
            } else {
                document.removeEventListener('mouseup', el.handleClickOutside);
            }
        }
    },
    unmounted(el) {
        // Remove event listener on unmount
        document.removeEventListener('mouseup', el.handleClickOutside);
    }
});

export default Closable;
