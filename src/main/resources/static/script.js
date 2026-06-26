const API = '/api';

// ---------- NAVEGAÇÃO ----------
function showSection(id) {
    document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
    document.querySelectorAll('nav button').forEach(b => b.classList.remove('active'));
    document.getElementById(id).classList.add('active');
    document.querySelector(`nav button[data-section="${id}"]`).classList.add('active');

    if (id === 'animais') {
        carregarProprietariosSelect();
        carregarAnimais();
    } else if (id === 'proprietarios') {
        carregarProprietarios();
    } else if (id === 'servicos') {
        carregarServicos();
    } else if (id === 'lancamentos') {
        carregarAnimaisSelect();
        carregarServicosSelect();
        carregarLancamentos();
    } else if (id === 'historico') {
        carregarAnimaisHistSelect();
        carregarServicosHistSelect();
    } else if (id === 'relatorio') {
        carregarProprietariosRelSelect();
    }
}

// Adiciona evento nos botões de navegação
document.querySelectorAll('nav button').forEach(btn => {
    btn.addEventListener('click', function () {
        const sectionId = this.getAttribute('data-section');
        showSection(sectionId);
    });
});

// ---------- MENSAGENS ----------
function showMsg(id, texto, tipo) {
    const el = document.getElementById(id);
    el.textContent = texto;
    el.className = 'msg ' + tipo;
    setTimeout(() => el.className = 'msg', 3000);
}

// ---------- REQUISIÇÕES ----------
async function get(url) {
    const r = await fetch(API + url);
    if (!r.ok) throw new Error('Erro na requisição');
    return r.json();
}

async function post(url, data) {
    return fetch(API + url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });
}

async function del(url) {
    return fetch(API + url, { method: 'DELETE' });
}

// ---------- DELETE GENÉRICO ----------
async function deletar(url, callback) {
    if (!confirm('Confirma exclusão?')) return;
    await del(url);
    callback();
}

// ---------- PROPRIETÁRIOS ----------
async function carregarProprietarios() {
    const lista = await get('/proprietarios');
    const tbody = document.querySelector('#tabela-proprietarios tbody');
    tbody.innerHTML = lista.map(p => `
        <tr>
            <td>${p.id}</td><td>${p.nome}</td><td>${p.email || ''}</td><td>${p.telefone || ''}</td>
            <td><button class="btn btn-red" onclick="deletar('/proprietarios/${p.id}', carregarProprietarios)">Excluir</button></td>
        </tr>`).join('');
}

async function salvarProprietario() {
    const nome = document.getElementById('prop-nome').value.trim();
    if (!nome) return showMsg('msg-prop', 'Nome é obrigatório.', 'error');
    const r = await post('/proprietarios', {
        nome,
        email: document.getElementById('prop-email').value,
        telefone: document.getElementById('prop-telefone').value,
        endereco: document.getElementById('prop-endereco').value
    });
    if (r.ok) {
        showMsg('msg-prop', 'Proprietário salvo com sucesso!', 'success');
        ['prop-nome','prop-email','prop-telefone','prop-endereco'].forEach(id => document.getElementById(id).value = '');
        carregarProprietarios();
    } else {
        showMsg('msg-prop', 'Erro ao salvar.', 'error');
    }
}

// ---------- ANIMAIS ----------
async function carregarProprietariosSelect() {
    const lista = await get('/proprietarios');
    const sel = document.getElementById('animal-proprietario');
    sel.innerHTML = lista.map(p => `<option value="${p.id}">${p.nome}</option>`).join('');
}

async function carregarAnimais() {
    const lista = await get('/animais');
    const tbody = document.querySelector('#tabela-animais tbody');
    tbody.innerHTML = lista.map(a => `
        <tr>
            <td>${a.id}</td><td>${a.nome}</td><td>${a.especie || ''}</td><td>${a.raca || ''}</td>
            <td>${a.proprietario?.nome || ''}</td>
            <td><button class="btn btn-red" onclick="deletar('/animais/${a.id}', carregarAnimais)">Excluir</button></td>
        </tr>`).join('');
}

async function salvarAnimal() {
    const nome = document.getElementById('animal-nome').value.trim();
    if (!nome) return showMsg('msg-animal', 'Nome é obrigatório.', 'error');

    const propId = document.getElementById('animal-proprietario').value;
    if (!propId || propId === '') {
        return showMsg('msg-animal', 'Selecione um proprietário.', 'error');
    }

    const animalData = {
        nome,
        especie: document.getElementById('animal-especie').value,
        raca: document.getElementById('animal-raca').value,
        idade: parseInt(document.getElementById('animal-idade').value) || null,
        sexo: document.getElementById('animal-sexo').value,
        peso: parseFloat(document.getElementById('animal-peso').value) || null,
        foto: document.getElementById('animal-foto').value,
        proprietario: { id: parseInt(propId, 10) }
    };

    console.log('Enviando animal:', animalData); // verifique no console do navegador

    try {
        const r = await post('/animais', animalData);
        if (r.ok) {
            showMsg('msg-animal', 'Animal salvo com sucesso!', 'success');
            ['animal-nome','animal-especie','animal-raca','animal-idade','animal-peso','animal-foto'].forEach(id => {
                document.getElementById(id).value = '';
            });
            // Não limpe o select do proprietário, pois é reutilizado
            carregarAnimais();
        } else {
            const errorText = await r.text();
            console.error('Erro ao salvar animal:', errorText);
            showMsg('msg-animal', 'Erro ao salvar. Veja o console.', 'error');
        }
    } catch (err) {
        console.error('Exceção ao salvar animal:', err);
        showMsg('msg-animal', 'Erro de conexão.', 'error');
    }
}

// ---------- SERVIÇOS ----------
async function carregarServicos() {
    const lista = await get('/servicos');
    const tbody = document.querySelector('#tabela-servicos tbody');
    tbody.innerHTML = lista.map(s => `
        <tr>
            <td>${s.id}</td><td>${s.nome}</td>
            <td>${s.tipo_servico || s.constructor?.name?.replace('Servico','') || ''}</td>
            <td>${s.descricao || ''}</td>
            <td>R$ ${s.preco?.toFixed(2)}</td>
            <td><button class="btn btn-red" onclick="deletar('/servicos/${s.id}', carregarServicos)">Excluir</button></td>
        </tr>`).join('');
}

async function salvarServico() {
    const nome = document.getElementById('servico-nome').value.trim();
    const preco = parseFloat(document.getElementById('servico-preco').value);
    const tipo = document.getElementById('servico-tipo').value;
    if (!nome || isNaN(preco) || !tipo) return showMsg('msg-servico', 'Nome, preço e tipo são obrigatórios.', 'error');
    const r = await post('/servicos', {
        nome,
        preco,
        descricao: document.getElementById('servico-descricao').value,
        tipo
    });
    if (r.ok) {
        showMsg('msg-servico', 'Serviço salvo com sucesso!', 'success');
        ['servico-nome','servico-preco','servico-descricao'].forEach(id => document.getElementById(id).value = '');
        document.getElementById('servico-tipo').value = '';
        carregarServicos();
    } else {
        showMsg('msg-servico', 'Erro ao salvar.', 'error');
    }
}

// ---------- LANÇAMENTOS ----------
async function carregarAnimaisSelect() {
    const lista = await get('/animais');
    const sel = document.getElementById('lanc-animal');
    sel.innerHTML = lista.map(a => `<option value="${a.id}">${a.nome} (${a.proprietario?.nome || ''})</option>`).join('');
}

async function carregarServicosSelect() {
    const lista = await get('/servicos');
    const sel = document.getElementById('lanc-servico');
    sel.innerHTML = lista.map(s => `<option value="${s.id}">${s.nome} - R$${s.preco?.toFixed(2)}</option>`).join('');
}

async function carregarLancamentos() {
    const lista = await get('/lancamentos');
    const tbody = document.querySelector('#tabela-lancamentos tbody');
    tbody.innerHTML = lista.map(l => `
        <tr>
            <td>${l.id}</td><td>${l.data}</td>
            <td>${l.animal?.nome || ''}</td><td>${l.servico?.nome || ''}</td>
            <td>R$ ${l.valor?.toFixed(2)}</td><td>${l.observacoes || ''}</td>
            <td><button class="btn btn-red" onclick="deletar('/lancamentos/${l.id}', carregarLancamentos)">Excluir</button></td>
        </tr>`).join('');
}

async function salvarLancamento() {
    const animalId = document.getElementById('lanc-animal').value;
    const servicoId = document.getElementById('lanc-servico').value;
    const data = document.getElementById('lanc-data').value;
    if (!animalId || !servicoId || !data)
        return showMsg('msg-lanc', 'Preencha todos os campos obrigatórios.', 'error');
    
    const r = await post('/lancamentos', {
        animalId: parseInt(animalId),
        servicoId: parseInt(servicoId),
        data: data,
        observacoes: document.getElementById('lanc-obs').value
    });
    if (r.ok) {
        showMsg('msg-lanc', 'Serviço registrado com sucesso!', 'success');
        ['lanc-data','lanc-obs'].forEach(id => document.getElementById(id).value = '');
        carregarLancamentos();
    } else {
        showMsg('msg-lanc', 'Erro ao registrar.', 'error');
    }
}

// ---------- HISTÓRICO POR ANIMAL ----------
async function carregarAnimaisHistSelect() {
    const lista = await get('/animais');
    const sel = document.getElementById('hist-animal');
    sel.innerHTML = lista.map(a => `<option value="${a.id}">${a.nome} (${a.proprietario?.nome || ''})</option>`).join('');
}

async function carregarServicosHistSelect() {
    const lista = await get('/servicos');
    const sel = document.getElementById('hist-servico');
    sel.innerHTML = '<option value="">Todos</option>' + 
        lista.map(s => `<option value="${s.id}">${s.nome}</option>`).join('');
}

async function buscarHistorico() {
    const animalId = document.getElementById('hist-animal').value;
    const inicio = document.getElementById('hist-inicio').value;
    const fim = document.getElementById('hist-fim').value;
    const servicoId = document.getElementById('hist-servico').value;
    
    if (!animalId) return alert('Selecione um animal.');
    
    let url = `/lancamentos/animal/${animalId}/historico?`;
    if (inicio) url += `dataInicio=${inicio}&`;
    if (fim) url += `dataFim=${fim}&`;
    if (servicoId) url += `servicoId=${servicoId}`;
    
    const lista = await get(url);
    const div = document.getElementById('historico-resultado');
    
    if (lista.length === 0) {
        div.innerHTML = '<div class="relatorio-box"><h3>Nenhum lançamento encontrado.</h3></div>';
        return;
    }
    
    div.innerHTML = `
        <div class="relatorio-box">
            <h3>Histórico do animal</h3>
            <table>
                <thead><tr><th>Data</th><th>Serviço</th><th>Valor</th><th>Observações</th></tr></thead>
                <tbody>
                    ${lista.map(l => `
                        <tr>
                            <td>${l.data}</td>
                            <td>${l.servico?.nome || ''}</td>
                            <td>R$ ${l.valor?.toFixed(2)}</td>
                            <td>${l.observacoes || ''}</td>
                        </tr>`).join('')}
                </tbody>
            </table>
        </div>`;
}

// ---------- RELATÓRIO ----------
async function carregarProprietariosRelSelect() {
    const lista = await get('/proprietarios');
    const sel = document.getElementById('rel-proprietario');
    sel.innerHTML = lista.map(p => `<option value="${p.id}">${p.nome}</option>`).join('');
}

async function gerarRelatorio() {
    const propId = document.getElementById('rel-proprietario').value;
    const inicio = document.getElementById('rel-inicio').value;
    const fim = document.getElementById('rel-fim').value;
    if (!propId || !inicio || !fim) return alert('Preencha todos os campos.');

    const div = document.getElementById('relatorio-resultado');
    div.innerHTML = '<p>Carregando...</p>';

    try {
        // Buscar o relatório completo
        const dto = await get(`/lancamentos/relatorio?proprietarioId=${propId}&dataInicio=${inicio}&dataFim=${fim}`);
        console.log('Relatório recebido:', dto);

        // Buscar nome do proprietário (paralelo opcional)
        let nomeProp = '';
        try {
            const prop = await get(`/proprietarios/${propId}`);
            nomeProp = prop.nome;
        } catch (e) {
            console.warn('Não foi possível carregar o nome do proprietário');
        }

        if (!dto || (!dto.totaisPorServico || dto.totaisPorServico.length === 0)) {
            div.innerHTML = '<div class="relatorio-box"><h3>Nenhum serviço encontrado no período.</h3></div>';
            return;
        }

        // Garantir que os arrays existam
        const totais = dto.totaisPorServico || [];
        const diario = dto.diarioPorServico || [];
        const totalGeral = dto.totalGeral || 0;

        let html = `
            <div class="relatorio-box">
                <h3>Cliente: ${nomeProp}</h3>
                <p style="color:#555; font-size:13px; margin-bottom:12px">
                    Período: ${inicio} a ${fim}
                </p>
                <h4>Total por serviço</h4>
                <table>
                    <thead><tr><th>Serviço</th><th>Total</th></tr></thead>
                    <tbody>
                        ${totais.map(t => `
                            <tr><td>${t.servico || '-'}</td><td>R$ ${(t.total || 0).toFixed(2)}</td></tr>
                        `).join('')}
                    </tbody>
                </table>
                <h4 style="margin-top:20px">Detalhamento diário</h4>
                <table>
                    <thead><tr><th>Data</th><th>Serviço</th><th>Total</th></tr></thead>
                    <tbody>
                        ${diario.length > 0 ? diario.map(d => `
                            <tr><td>${d.data || ''}</td><td>${d.servico || '-'}</td><td>R$ ${(d.total || 0).toFixed(2)}</td></tr>
                        `).join('') : '<tr><td colspan="3">Nenhum detalhamento diário disponível</td></tr>'}
                    </tbody>
                </table>
                <div class="relatorio-total">Total geral: R$ ${totalGeral.toFixed(2)}</div>
            </div>`;

        div.innerHTML = html;

    } catch (err) {
        console.error('Erro ao gerar relatório:', err);
        div.innerHTML = '<div class="msg error">Erro ao carregar o relatório. Verifique o console (F12).</div>';
    }
}

// Carregar a seção inicial (proprietários)
carregarProprietarios();